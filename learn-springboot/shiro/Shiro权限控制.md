# Shiro权限控制
使用Shiro权限控制，核心点在于使用自定义Filter拦截请求，然后执行Realm中的认证和授权方法实现权限的控制。

## 引入依赖

```xml
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <!--mybatis-plus-->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.4.0</version>
    </dependency>

    <!-- 实现对 Shiro 的自动化配置 -->
    <dependency>
        <groupId>org.apache.shiro</groupId>
        <artifactId>shiro-spring-boot-starter</artifactId>
        <version>1.7.1</version>
    </dependency>

    <dependency>
        <groupId>org.hibernate.validator</groupId>
        <artifactId>hibernate-validator</artifactId>
    </dependency>

</dependencies>
```

## 新建配置类，注入相关Bean

在配置类中注入`DefaultWebSecurityManager`bean和`ShiroFilterFactoryBean` Bean，前者为了配置自定义的Realm类，后者为了配置自定义的Filter过滤器。

```java
@Configuration
public class ShiroConfig {

    @Bean
    public OAuth2Realm oAuth2Realm() {
        return new OAuth2Realm();
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager() {
        // 创建 DefaultWebSecurityManager 对象
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置其使用的 Realm 为 OAuth2Realm
        securityManager.setRealm(this.oAuth2Realm());
        // 无需使用记住密码功能
        securityManager.setRememberMeManager(null);
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        // 创建 ShiroFilterFactoryBean 对象，用于创建 ShiroFilter 过滤器
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();

        // 设置 SecurityManager
        shiroFilter.setSecurityManager((SecurityManager) securityManager);

        // <1> 创建 OAuth2Filter 过滤器，并设置名字为 oauth2。
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new OAuth2Filter());
        shiroFilter.setFilters(filters);

        // <2>
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/app/**", "anon");
        filterMap.put("/sys/login", "anon"); // <3> 登录接口
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/aaa.txt", "anon");
        filterMap.put("/**", "oauth2"); // <4> 默认剩余的 URL ，需要经过认证
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }
}
```
## 编写自定义过滤器类

自定义实现`BasicHttpAuthenticationFilter`接口的`Filter`过滤器类，重写`isAccessAllowed()`、`onAccessDenied()`、`onLoginFailure()`、`createToken()`

* `isAccessAllowed()`方法用来判断是否可以通过该filter过滤器
* `onAccessDenied()`方法用来处理isAccessAllowed()方法返回false后的流程，可以在这里执行登录
* `onLoginFailure()`方法用来处理登录发生异常后的流程，在该方法中捕获异常，将异常返回给前端
* `createToken()`方法用来生成shiro框架中的token，以便在`Realm实现类中获取该Token`。

```java
public class OAuth2Filter extends BasicHttpAuthenticationFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        // <1> 获取请求中的 token
        String token = getRequestToken((HttpServletRequest) request);
        // 如果不存在，则返回 null
        if (StringUtils.isBlank(token)) {
            return null;
        }

        // <2> 创建 OAuth2Token 对象
        return new OAuth2Token(token);
    }

    private String getRequestToken(HttpServletRequest httpRequest) {
        // 优先，从 header 中获取 token
        String token = httpRequest.getHeader("token");

        // 次之，如果 header 中不存在 token ，则从参数中获取 token
        if (StringUtils.isBlank(token)) {
            token = httpRequest.getParameter("token");
        }
        return token;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 只允许Option方法可以直接访问，否则进入onAccessDenied()方法进行认证
        return ((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name());
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // <1> 获取请求中的 token 。如果 token 不存在，直接返回 401 ，认证不通过
        String token = getRequestToken((HttpServletRequest) request);
        if (StringUtils.isBlank(token)) {
            // 设置响应 Header
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

            // 返回认证不通过
            String json = "invalid token";
            httpResponse.getWriter().print(json);

            // 返回 false
            return false;
        }

        // <2> 执行登录逻辑，实际执行的是基于 Token 进行认证。
        // 调用父类 AuthenticatingFilter 的 #executeLogin(request, response) 方法，执行登录逻辑。
        // 实际上在方法内部，调用 OAuth2Realm 的 #doGetAuthenticationInfo(AuthenticationToken token) 方法，
        // 执行基于 Token 进行认证
        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        // 设置响应 Header
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        try {
            // 处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();

            // 返回认证不通过
            String json = "认证失败";
            httpResponse.getWriter().print(json);
        } catch (IOException ignored) {
        }

        // 返回 false
        return false;
    }
}
```
## 编写自定义Realm实现类

`Realm接口`是整个认证框架中的一个组件，用来提供`认证和授权`的实现接口，所以我们需要实现继承`AuthorizingRealm`抽象类的实现类，重写`doGetAuthenticationInfo()`、`doGetAuthorizationInfo()`

* `doGetAuthenticationInfo()`方法用来认证用户信息，该方法的传入参数为`AuthenticationToken`类（即我们自定义的Token类），返回用户认证信息`AuthenticationInfo`类

* `doGetAuthorizationInfo()`方法用来授权，该方法的传入参数为`PrincipalCollection`类，该类包含了用户的一些信息，可以在方法中获取用户信息；返回用户授权信息`AuthorizationInfo`类，该类包含了用户所拥有的权限和角色。

> 注意：如果自定义了Token类，需要重写Realm的supports接口，使其支持自定义Token。



```java
public class OAuth2Realm extends AuthorizingRealm {
    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 重写支持的token类型
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 拿到token信息
        String accessToken = (String) token.getPrincipal();
        // <1> 根据 accessToken ，查询用户信息
        SysUserTokenEntity tokenEntity = sysUserTokenService.queryByToken(accessToken);
        // token 失效
        if (tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        // <2> 查询用户信息
        SysUserEntity user = sysUserService.getById(tokenEntity.getUserId());
        // 账号锁定
        if (user.getStatus() == 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        // <3> 创建 SimpleAuthenticationInfo 对象
        return new SimpleAuthenticationInfo(user, accessToken, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // <1> 获得 SysUserEntity 对象
        SysUserEntity user = (SysUserEntity) principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        // <2> 用户权限列表
        Set<String> permsSet = sysUserService.getUserPermissions(userId);

        // <3> 创建 SimpleAuthorizationInfo 对象
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }
}
```

## Shiro常用的权限注解

* `@RequiresPermissions`：当前Subject必须拥有指定权限
* `@RequiresRoles`: 当前Subject必须拥有指定角色
* `@RequiresAuthentication`：当前Subject必须经过认证
* `@RequiredGuest`: 当前Subject可以不经过认证

* `@RequiredUser`: 当前Subject必须是系统用户
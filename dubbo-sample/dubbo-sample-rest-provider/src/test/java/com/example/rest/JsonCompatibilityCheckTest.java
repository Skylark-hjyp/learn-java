package com.example.rest;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.rpc.Exporter;
import org.apache.dubbo.rpc.Protocol;
import org.apache.dubbo.rpc.ProxyFactory;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.model.ModuleServiceRepository;
import org.apache.dubbo.rpc.model.ProviderModel;
import org.apache.dubbo.rpc.model.ServiceDescriptor;

import com.example.rest.compatibility.RestDemoServiceImpl;
import com.example.rest.compatibility.RestDemoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class JsonCompatibilityCheckTest {

    private final int availablePort = NetUtils.getAvailablePort();
    private final URL exportUrl = URL.valueOf("rest://127.0.0.1:" + availablePort + "/rest?interface=com.example.rest.compatibility.RestDemoService");
    private final ModuleServiceRepository repository = ApplicationModel.defaultModel().getDefaultModule().getServiceRepository();

    private final Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension("rest");
    private final ProxyFactory proxy = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();


    @Test
    public void testJsonCheckDisabled() {

        RestDemoService server = new RestDemoServiceImpl();

        URL url = this.registerProvider(exportUrl, server, RestDemoService.class);

        url = url.addParameter("jsonCheckLevel", "disabled");

        Exporter<RestDemoService> exporter = protocol.export(proxy.getInvoker(server, RestDemoService.class, url));

        exporter.unexport();
    }

    @Test
    public void testJsonCheckWarn() {
        RestDemoService server = new RestDemoServiceImpl();

        URL url = this.registerProvider(exportUrl, server, RestDemoService.class);

        url = url.addParameter("jsonCheckLevel", "warn");

        Exporter<RestDemoService> exporter = protocol.export(proxy.getInvoker(server, RestDemoService.class, url));

        exporter.unexport();
    }

    @Test
    public void testJsonCheckStrict() {
        RestDemoService server = new RestDemoServiceImpl();

        URL url = this.registerProvider(exportUrl, server, RestDemoService.class);

        URL newUrl =  url.addParameter("jsonCheckLevel", "strict");

        Exporter<RestDemoService> exporter = null;
        try {
            exporter = protocol.export(proxy.getInvoker(server, RestDemoService.class, newUrl));
        } finally {
            if (exporter != null) exporter.unexport();
        }

        // Assertions.assertThrowsExactly(IllegalStateException.class, () -> {
        //
        // });
    }

    private URL registerProvider(URL url, Object impl, Class<?> interfaceClass) {
        ServiceDescriptor serviceDescriptor = repository.registerService(interfaceClass);
        ProviderModel providerModel = new ProviderModel(
            url.getServiceKey(),
            impl,
            serviceDescriptor,
            null,
            null);
        repository.registerProvider(providerModel);
        return url.setServiceModel(providerModel);
    }
}

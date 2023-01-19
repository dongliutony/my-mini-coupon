package com.dongliu.coupon;

import com.dongliu.coupon.permission.PermissionClient;
import com.dongliu.coupon.vo.PermissionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * <h1>permission detect listener, auto run after microservice Spring boot up</h1>
 */
@Slf4j
@Component
public class PermissionDetectListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final String KEY_SERVER_CTX = "server.servlet.context-path";
    private static final String KEY_SERVICE_NAME = "spring.application.name";

    /**
     * Run task in a separate thread, to not block microservice
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ApplicationContext context = event.getApplicationContext();
        new Thread(() -> {
            // scan permission
            List<PermissionInfo> infoList = scanPermission(context);

            // register permission
            registerPermission(infoList, context);
        }).start();
    }

    /**
     * <h2>register permission of interfaces</h2>
     */
    private void registerPermission(List<PermissionInfo> infoList, ApplicationContext ctx) {
        log.info("*************** registering permission... ***************");

        PermissionClient permissionClient = ctx.getBean(PermissionClient.class);
        if(null == permissionClient) {
            log.error("no permissionClient bean found");
            return;
        }

        // get service name
        String serviceName = ctx.getEnvironment().getProperty(KEY_SERVICE_NAME);
        log.info("serviceName: {}", serviceName);
        boolean result = new PermissionRegistry(permissionClient, serviceName).register(infoList);

        if (result) {
            if (result) {
                log.info("*************** done register ***************");
            }
        }
    }

    /**
     * <h2>Scan microservice controller</h2>
     */
    private List<PermissionInfo> scanPermission(ApplicationContext ctx) {
        log.info("*************** scanning permission... ***************");

        // get prefix from context
        String pathPrefix = ctx.getEnvironment().getProperty(KEY_SERVER_CTX);

        // get Spring's mapping bean
        // NOTE: cannot use ctx.getBean(RequestMappingHandlerMapping.class), it has more than one bean name.
        RequestMappingHandlerMapping mappingBean =
                (RequestMappingHandlerMapping) ctx.getBean("requestMappingHandlerMapping");

        // scan
        List<PermissionInfo> permissionInfoList =
                new AnnotationScanner(pathPrefix).scanPermission(mappingBean.getHandlerMethods());

        permissionInfoList.forEach(p -> log.info("{}", p));

        log.info("{} permission found", permissionInfoList.size());
        log.info("*************** done scanning ***************");

        return permissionInfoList;
    }
}

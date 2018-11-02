package winelocator.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;


/**
 * <p>
 *   Custom {@link EnableAutoConfiguration Auto-configuration} for Thymeleaf 3.<br>
 * </p>
 * <p>
 *   This configuration file exists for those who install Thymeleaf 3 independently
 *   instead of adding Spring Boot Thymeleaf dependency ({@code spring-boot-starter-thymeleaf})
 *   and want to use Thymeleaf 3 and its full features
 *   on Spring Boot version 1.5.4-RELEASE and under.
 *   Spring Boot auto configuration for Thymeleaf is designed and built on Thymeleaf 2,
 *   so it doesn't fully support Thymeleaf 3 features.
 * </p>
 * <p>
 *   You must disable {@link org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration ThymeleafAutoConfiguration}
 *   when launching Spring Boot application using {@code exclude} property of @SpringBootApplication
 *   to use this custom configuration.
 * </p>
 * <p>
 *   This doesn't enable configuration property {@link org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties ThymeleafProperties}
 *   which is used to read and manipulate Thymeleaf related options via Spring appplication properties,
 *   so all the Thymeleaf options should be set in this java code directly.
 * </p>
 * <p>
 *   As this configuration is meant to turn off the original auto-configuration in Spring Boot,
 *   which means the application properties for Thymeleaf will be turned off as well,
 *   the cache option wouldn't be set to false automatically even if you use Spring Boot Devtools.
 *   So you have to set it here manually when you try to use auto-reloading template feature.
 * </p>
 * <p>
 *   It is anticipated this configuration bean will not be required when Spring Boot 2 comes to the static.
 * </p>
 *
 * @author DJuno (djkehh@gmail.com)
 */
@Configuration
@EnableConfigurationProperties(Thymeleaf3Config.Thymeleaf3Properties.class)
@ConditionalOnClass(SpringTemplateEngine.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class Thymeleaf3Config implements ApplicationContextAware {

    private ApplicationContext applicationContext = null;

    @Autowired
    private Thymeleaf3Properties properties;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();

        resolver.setTemplateEngine(thymeleafTemplateEngine());
        resolver.setCharacterEncoding("UTF-8");

        return resolver;
    }

    @Bean
    public ITemplateEngine thymeleafTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();

        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolver(thymeleafTemplateResolver());

        return engine;
    }

    @Bean
    public ITemplateResolver thymeleafTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();

        resolver.setApplicationContext(this.applicationContext);
        resolver.setPrefix(this.properties.getPrefix());
        resolver.setSuffix(this.properties.getSuffix());
        resolver.setTemplateMode(this.properties.getMode());
        resolver.setUseDecoupledLogic(this.properties.isDecoupledLogic());
        if (this.properties.getEncoding() != null) {
            resolver.setCharacterEncoding(this.properties.getEncoding().name());
        }
        resolver.setCacheable(this.properties.isCache());
        Integer order = this.properties.getTemplateResolverOrder();
        if (order != null) {
            resolver.setOrder(order);
        }

        return resolver;
    }

    /**
     * <p>
     *   Properties for Thymeleaf 3.
     * </p>
     *
     * @see org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties
     * @author DJuno (djkehh@gmail.com)
     */
    @ConfigurationProperties(prefix = "spring.thymeleaf")
    public class Thymeleaf3Properties extends ThymeleafProperties {

        /**
         * Template mode to be applied to templates. Default value is {@code HTML} from Thymeleaf 3.
         */
        private String mode = TemplateMode.HTML.name();

        /**
         * Enable decoupling template logic. Default value is {@code false}.
         */
        private boolean decoupledLogic = false;

        @Override
        public String getMode() {
            return mode;
        }

        @Override
        public void setMode(String mode) {
            this.mode = mode;
        }

        public boolean isDecoupledLogic() {
            return decoupledLogic;
        }

        public void setDecoupledLogic(boolean decoupledLogic) {
            this.decoupledLogic = decoupledLogic;
        }

    }

}
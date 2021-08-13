package zhangyu.fool.generator.module;

import com.google.inject.AbstractModule;

/**
 * @author xiaomigzhang
 * @date: 2021/08/13
 */
public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModule());
    }
}

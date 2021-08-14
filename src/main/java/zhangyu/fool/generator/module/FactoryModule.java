package zhangyu.fool.generator.module;

import com.google.inject.AbstractModule;
import zhangyu.fool.generator.dao.DataAccesser;

/**
 * @author xiaomingzhang
 * @date: 2021/08/13
 */
public class FactoryModule extends AbstractModule {

    @Override
    protected void configure() {
        //bind(DataAccesser.class).to(DataAccesser.class);
    }
}

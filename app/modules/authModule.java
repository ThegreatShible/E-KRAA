package modules;

import authentication.MyDeadboldHandler;
import authentication.MyHandlerCache;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.cache.HandlerCache;
import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import play.Configuration;
import play.Environment;
import play.api.inject.Module;
import scala.collection.immutable.Seq;

import javax.inject.Singleton;

public class authModule extends AbstractModule
{


    @Override
    protected void configure() {
        /*bind(DeadboltHandler.class).to(MyDeadboldHandler.class).asEagerSingleton();
        bind(HandlerCache.class).to(MyHandlerCache.class).asEagerSingleton();
    */
    }
}

package authentication;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.cache.HandlerCache;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MyHandlerCache implements HandlerCache {

    final private DeadboltHandler deadboltHandler;
    @Inject
    public MyHandlerCache(DeadboltHandler deadboltHandler) {
          this.deadboltHandler = deadboltHandler;
    }
    @Override
    public DeadboltHandler apply(String s) {
        return deadboltHandler;
    }

    @Override
    public DeadboltHandler get() {
        return deadboltHandler;
    }
}

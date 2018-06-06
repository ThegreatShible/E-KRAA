package controllers;

import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class LangController extends Controller {

    @Inject
    private MessagesApi messagesApi;

    public Result changeLanguage(String lang) {
        if (lang.equals("ar") || lang.equals("fr") || lang.equals("en")) {
            changeLang(lang);
            return ok("done");
        } else {
            return notFound();
        }
    }


}

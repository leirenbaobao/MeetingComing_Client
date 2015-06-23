package com.ctrlz.meetingcoming.httpaction;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLStreamHandlerFactory;

import android.annotation.SuppressLint;
import com.huawei.svn.sdk.thirdpart.SvnURLStreamHandlerFactory;

@SuppressLint("NewApi")
public class URLConnectionFactoryHelper
{

    private static URLStreamHandlerFactory factory;

    public synchronized static void setURLStreamHandlerFactory()
    {
        if (factory == null)
        {
            factory = new SvnURLStreamHandlerFactory();
            URL.setURLStreamHandlerFactory(factory);

            CookieHandler.setDefault(new CookieManager(null,
                    CookiePolicy.ACCEPT_ALL));
        }
    }

}

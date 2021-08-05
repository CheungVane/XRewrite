package com.XRewrite.utils;

import com.XRewrite.cache.*;

public class ResetManager {

    public static void clean(){
        CoverageCache.clean();
        NormalizingAuxCache.clean();
        RenamingCache.clean();
        ReportUtils.clean();
        SubsumingCache.clean();
    }
}

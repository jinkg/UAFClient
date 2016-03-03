package com.yalin.fidoclient.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by YaLin on 2016/1/13.
 */
public class Utils {
    public static String getFacetId(Context context, int callingUid) {
        String packageNames[] = context.getPackageManager().getPackagesForUid(callingUid);

        if (packageNames == null) {
            return null;
        }
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageNames[0], PackageManager.GET_SIGNATURES);

            byte[] cert = info.signatures[0].toByteArray();
            InputStream input = new ByteArrayInputStream(cert);

            CertificateFactory cf = CertificateFactory.getInstance("X509");
            X509Certificate c = (X509Certificate) cf.generateCertificate(input);

            MessageDigest md = MessageDigest.getInstance("SHA1");

            return "android:apk-key-hash:" +
                    Base64.encodeToString(md.digest(c.getEncoded()), Base64.DEFAULT | Base64.NO_WRAP | Base64.NO_PADDING);
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
        }

        return null;
    }
}

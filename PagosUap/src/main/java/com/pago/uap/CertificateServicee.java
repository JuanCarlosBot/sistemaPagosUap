package com.pago.uap;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

import com.itextpdf.text.DocumentException;

public interface CertificateServicee {
    public void generateCertificate(String codigo, Long id_persona, Date finicio, Date ffin) throws GeneralSecurityException, IOException, DocumentException;
}

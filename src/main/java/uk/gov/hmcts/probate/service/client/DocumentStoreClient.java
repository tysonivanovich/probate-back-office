package uk.gov.hmcts.probate.service.client;

import lombok.extern.slf4j.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.springframework.stereotype.*;
import uk.gov.hmcts.probate.model.ccd.raw.*;
import uk.gov.hmcts.probate.security.*;

import java.io.*;

@Component
@Slf4j
public class DocumentStoreClient {

    private CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
    private static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";
    private static final String USER_ID = "user-id";

    public byte[] retrieveDocument(Document document, String authHeaderValue) throws IOException {

        byte[] bytes = null;
        try {
            HttpGet request = new HttpGet(document.getDocumentLink().getDocumentBinaryUrl());
            request.setHeader(SERVICE_AUTHORIZATION, authHeaderValue);
            request.setHeader(USER_ID, document.getDocumentGeneratedBy());
            log.info("About to retrieve " + document + " from dm-store with binary url: "
                    + document.getDocumentLink().getDocumentBinaryUrl());
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(request);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            closeableHttpResponse.getEntity().writeTo(byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
            log.info("Successfully retrieved " + document + " from dm-store with binary url: "
                    + document.getDocumentLink().getDocumentBinaryUrl());
            log.info("document bytes " + document + " with bytes: " + bytes);


        } catch (IOException e) {

            log.error("Failed to get bytes from document store for document {} in case Id {}",
                    document.getDocumentLink().getDocumentBinaryUrl());
            throw new IOException("Failed to get bytes from document store for "
                    + document.getDocumentFileName() + "with url "
                    + document.getDocumentLink().getDocumentBinaryUrl());
        }
        return bytes;
    }

    public byte[] retrieveUploadDocument(ScannedDocument document, String authHeaderValue, SecurityDTO securityDTO) throws IOException {

        byte[] bytes = null;
        try {
            HttpGet request = new HttpGet(document.getUrl().getDocumentBinaryUrl());
            request.setHeader(SERVICE_AUTHORIZATION, authHeaderValue);
            request.setHeader(USER_ID, securityDTO.getUserId());
            log.info("About to retrieve " + document + " from dm-store with binary url: "
                    + document.getUrl().getDocumentBinaryUrl());
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(request);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            closeableHttpResponse.getEntity().writeTo(byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
            log.info("Successfully retrieved " + document + " from dm-store with binary url: "
                    + document.getUrl().getDocumentBinaryUrl());


        } catch (IOException e) {

            log.error("Failed to get bytes from document store for document {} in case Id {}",
                    document.getUrl().getDocumentBinaryUrl());
            throw new IOException("Failed to get bytes from document store for "
                    + document.getSubtype() + "with url "
                    + document.getUrl().getDocumentBinaryUrl());
        }
        return bytes;
    }
}

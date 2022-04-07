package icu.cyclone.bancleaner.client.whois;

import icu.cyclone.bancleaner.BaseIntegrationTest;
import icu.cyclone.bancleaner.ResourceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static icu.cyclone.bancleaner.TestObjects.TEMPLATE_WHO_IS_DTO;
import static icu.cyclone.bancleaner.TestObjects.TEMPLATE_WHO_IS_DTO_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.cloud.openfeign.encoding.HttpEncoding.CONTENT_TYPE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

class IpWhoisClientTest extends BaseIntegrationTest {
    private static final String PATH_WHOIS = "client/whois/";
    private static final String PATH_WHOIS_8_RESPONSE = PATH_WHOIS + "success8.json";
    private static final String PATH_WHOIS_RESPONSE_ERROR = PATH_WHOIS + "error.json";

    @Autowired
    public IpWhoisClient client;

    @Test
    public void getWhoIsTest() {
        wireMockServer.stubFor(get(urlEqualTo("/json/8.8.8.8"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withBody(ResourceUtils.readFile(PATH_WHOIS_8_RESPONSE))
            ));
        var response = client.getWhoIs("8.8.8.8");
        assertEquals(TEMPLATE_WHO_IS_DTO, response);
    }

    @Test
    public void getWhoIsFailTest() {
        wireMockServer.stubFor(get(urlEqualTo("/json/10.8.8.8"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withBody(ResourceUtils.readFile(PATH_WHOIS_RESPONSE_ERROR))
            ));
        var response = client.getWhoIs("10.8.8.8");
        assertEquals(TEMPLATE_WHO_IS_DTO_ERROR, response);
    }
}
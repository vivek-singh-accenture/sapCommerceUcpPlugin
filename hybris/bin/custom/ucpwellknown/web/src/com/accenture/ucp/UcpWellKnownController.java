package com.accenture.ucp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Serves the UCP discovery document at /.well-known/ucp for the SAP Commerce
 * electronics store. Points UCP-aware shopping agents at the ucpAdaptor MCP
 * endpoint which translates tool calls to OCC v2 API requests.
 */
@Controller
@RequestMapping("/ucp")
public class UcpWellKnownController {

    @Value("${ucpAdaptorBaseUrl:http://localhost:8080}")
    private String ucpAdaptorBaseUrl;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getUcpProfile() {
        String mcpEndpoint = ucpAdaptorBaseUrl.stripTrailing().replaceAll("/+$", "") + "/ucp/sap-mcp";
        return """
                {
                  "ucp": {
                    "version": "2026-08-25",
                    "services": {
                      "dev.ucp.shopping": [
                        {
                          "version": "2026-08-25",
                          "spec": "https://ucp.dev/2026-08-25/specification/overview",
                          "transport": "mcp",
                          "endpoint": "%s",
                          "schema": "https://ucp.dev/2026-08-25/services/shopping/mcp.openrpc.json"
                        }
                      ]
                    },
                    "capabilities": {
                      "dev.ucp.shopping.catalog.search": [
                        {
                          "version": "2026-08-25",
                          "spec": "https://ucp.dev/2026-08-25/specification/shopping/catalog/search",
                          "schema": "https://ucp.dev/2026-08-25/schemas/shopping/catalog_search.json"
                        }
                      ],
                      "dev.ucp.shopping.cart": [
                        {
                          "version": "2026-08-25",
                          "spec": "https://ucp.dev/2026-08-25/specification/shopping/cart",
                          "schema": "https://ucp.dev/2026-08-25/schemas/shopping/cart.json"
                        }
                      ]
                    },
                    "payment_handlers": {}
                  }
                }
                """.formatted(mcpEndpoint);
    }
}

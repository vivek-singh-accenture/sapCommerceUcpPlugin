# SAP Commerce UCP Plugin

Custom SAP Commerce 2205 extensions that expose a SAP Commerce store to UCP
(Universal Commerce Protocol) shopping agents.

## Contents

| Path | Description |
|------|-------------|
| `hybris/bin/custom/ucpwellknown` | Web extension serving the UCP discovery profile at `/.well-known/ucp`. Advertises the ucpAdaptor MCP endpoint (`<ucpAdaptorBaseUrl>/ucp/sap-mcp`) plus the catalog search and cart capabilities. |
| `hybris/bin/custom/yb2bacceleratorstorefront` | B2B accelerator storefront generated from the `yb2bacceleratorstorefront` template. |
| `hybris/config/localextensions.xml` | Extension list (cx recipe + `ucpwellknown`). |
| `hybris/config/local.properties.template` | Template for `local.properties` (the real file is git-ignored). |

Only custom code and config are tracked. The SAP platform, installer, data,
logs, build output and addon files copied in at build time are excluded via
`.gitignore`.

## Setup

1. Install SAP Commerce 2205 (patch 52 used here) with the `cx` installer recipe,
   so that `hybris/bin/modules`, `hybris/bin/platform` etc. exist.
2. Clone this repo over the install root (the directory that contains `hybris/`):
   ```sh
   cd <sapcommerce-root>
   git init
   git remote add origin git@github-work:vivek-singh-accenture/sapCommerceUcpPlugin.git
   git fetch origin
   git checkout -f -t origin/main
   ```
3. Create `local.properties` from the template and fill in the `<CHANGE_ME>` values:
   ```sh
   cp hybris/config/local.properties.template hybris/config/local.properties
   ```
4. Add the storefront hostnames to `/etc/hosts`:
   ```
   127.0.0.1 electronics.local apparel-uk.local apparel-de.local powertools.local
   ```
5. Build and initialize:
   ```sh
   cd hybris/bin/platform
   . ./setantenv.sh
   ant clean all
   ant initialize   # first time only; wipes the database
   ./hybrisserver.sh
   ```

## Endpoints

| URL | Description |
|-----|-------------|
| `https://localhost:9002/.well-known/ucp` | UCP discovery profile |
| `https://localhost:9002/hac` | Admin console |
| `https://localhost:9002/backoffice` | Backoffice |
| `https://electronics.local:9002/yacceleratorstorefront` | Electronics B2C storefront |
| `https://powertools.local:9002/yb2bacceleratorstorefront` | Powertools B2B storefront |

The MCP endpoint itself is served by the separate ucpAdaptor service
(default `http://localhost:8080`, configurable via `ucpAdaptorBaseUrl` in
`local.properties`).

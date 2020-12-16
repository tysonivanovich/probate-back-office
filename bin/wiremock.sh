#!/usr/bin/env bash

# Setup Wiremock responses for Professional Reference Data based on existing Idam users

# pba account successful
curl -X POST \
--data '{
          "request": {
            "method": "POST",
            "url": "/credit-account-payments",
            "bodyPatterns": [ {
              "contains": "PBA0087535"
              }
            ],
            "headers": {
              "Content-Type": {
                "equalTo": "application/json"
              }
            }
          },
          "response": {
            "status": 201,
            "headers": {
              "Content-Type": "application/json"
            },
            "jsonBody": {
                "reference": "RC-1590-6786-1063-9996",
                "date_created": "2020-05-28T15:10:10.694+0000",
                "status": "Success",
                "payment_group_reference": "2020-1590678609071",
                "status_histories": [
                  {
                    "status": "success",
                    "date_created": "2020-05-28T15:10:10.700+0000",
                    "date_updated": "2020-05-28T15:10:10.700+0000"
                  }
                ]
            }
          }
        }' \
http://localhost:8991/__admin/mappings/new

# pba account successful
curl -X POST \
--data '{
          "request": {
            "method": "POST",
            "url": "/credit-account-payments",
            "bodyPatterns": [ {
              "contains": "PBA0088063"
              }
            ],
            "headers": {
              "Content-Type": {
                "equalTo": "application/json"
              }
            }
          },
          "response": {
            "status": 201,
            "headers": {
              "Content-Type": "application/json"
            },
            "jsonBody": {
                "reference": "RC-1590-6786-1063-9996",
                "date_created": "2020-05-28T15:10:10.694+0000",
                "status": "Success",
                "payment_group_reference": "2020-1590678609071",
                "status_histories": [
                  {
                    "status": "success",
                    "date_created": "2020-05-28T15:10:10.700+0000",
                    "date_updated": "2020-05-28T15:10:10.700+0000"
                  }
                ]
            }
          }
        }' \
http://localhost:8991/__admin/mappings/new

# pba account on hold
curl -X POST \
--data '{
          "request": {
            "method": "POST",
            "url": "/credit-account-payments",
            "bodyPatterns": [ {
              "contains": "PBA0087442"
              }
            ],
            "headers": {
              "Content-Type": {
                "equalTo": "application/json"
              }
            }
          },
          "response": {
            "status": 403,
            "headers": {
              "Content-Type": "application/json"
            },
            "jsonBody": {
                "reference": "RC-1590-6786-1063-9996",
                "date_created": "2020-05-28T15:10:10.694+0000",
                "status": "Failed",
                "payment_group_reference": "2020-1590678609071",
                "status_histories": [
                  {
                    "status": "failed",
                    "error_code": "CA-E0004",
                    "error_message": "Your account is on hold",
                    "date_created": "2020-05-28T15:10:10.700+0000",
                    "date_updated": "2020-05-28T15:10:10.700+0000"
                  }
                ]
            }
          }
        }' \
http://localhost:8991/__admin/mappings/new

# pba account deleted
curl -X POST \
--data '{
          "request": {
            "method": "POST",
            "url": "/credit-account-payments",
            "bodyPatterns": [ {
              "contains": "PBA0087240"
              }
            ],
            "headers": {
              "Content-Type": {
                "equalTo": "application/json"
              }
            }
          },
          "response": {
            "status": 403,
            "headers": {
              "Content-Type": "application/json"
            },
            "jsonBody": {
                "reference": "RC-1590-6786-1063-9996",
                "date_created": "2020-05-28T15:10:10.694+0000",
                "status": "Failed",
                "payment_group_reference": "2020-1590678609071",
                "status_histories": [
                  {
                    "status": "failed",
                    "error_code": "CA-E0004",
                    "error_message": "Your account is deleted",
                    "date_created": "2020-05-28T15:10:10.700+0000",
                    "date_updated": "2020-05-28T15:10:10.700+0000"
                  }
                ]
            }
          }
        }' \
http://localhost:8991/__admin/mappings/new

#PBA accounts
curl -X POST \
--data '{
          "request": {
            "method": "GET",
            "urlPath": "/refdata/external/v1/organisations/pbas"
          },
          "response": {
            "status": 200,
            "headers": {
              "Content-Type": "application/json"
            },
            "jsonBody": {
              "organisationEntityResponse" : {
                "organisationIdentifier": "0UFUG4Z",
                "name": "ia-legal-rep-org",
                "status": "ACTIVE",
                "sraId": null,
                "sraRegulated": false,
                "companyNumber": null,
                "companyUrl": null,
                "superUser": {
                  "firstName": "legalrep",
                  "lastName": "orgcreator",
                  "email": "'"${TEST_LAW_FIRM_SHARE_CASE_ORG_USERNAME}"'"
                },
                "paymentAccount": [
                  "PBA0087535",
                  "PBA0087240",
                  "PBA0088063",
                  "PBA0087442"
                ],
                "contactInformation": null
              }
            }
          }
        }' \
http://localhost:8991/__admin/mappings/new


# make responses persistent in Docker volume
curl -X POST http://localhost:8991/__admin/mappings/save
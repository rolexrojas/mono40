# tPago Android App
TODO

# Jira
> QA: https://jira.gcs-systems.com:8080/secure/IssueNavigator.jspa?reset=true&jqlQuery=project+%3D+APTPII+AND+resolution+%3D+Unresolved+AND+assignee+%3D+tpagogbh+ORDER+BY+priority+DESC&mode=hide

> DEV: https://jira.gcs-systems.com:8080/secure/IssueNavigator.jspa?reset=true&jqlQuery=project+%3D+TPAPPPRD+AND+resolution+%3D+Unresolved+AND+assignee+%3D+tpagogbh+ORDER+BY+priority+DESC&mode=hide

> GBH: https://gbhapps.atlassian.net/secure/RapidBoard.jspa?rapidView=32&projectKey=TP&selectedIssue=TP-62


# Zeplin

Link:
> https://app.zeplin.io/profile/account

User:
> r.amarante@gbh.com.do

Password:
> qwerty123

# Github repo
> https://github.com/GCS/tPago-mobile-2.0-Android

> ASK for credentians in npass

# Apiary

> https://mobileappws.docs.apiary.io/#reference/0/initial-load/list-of-all-the-transactions

# Slack

> tpago.slack.com

# Postman

* https://www.getpostman.com/collections/84c05c80cebdcd71f2da
* https://www.getpostman.com/collections/aee176d802818c1b7f5c
* https://www.getpostman.com/collections/15c375fd075b7511fad8
* https://www.getpostman.com/collections/bf8c1d0b4f38e39b1d04

### DEVT Enviroment
baseUrl:http://tpagonet-dev.gcs-systems.com/api/neo/
userPhoneNumber:8099521818
sessionToken:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0MTE6ODA5OTUyMTgxODowMTIzNDU2Nzg5MDEyMzQ1Njc4OSIsImV4cCI6MTUyOTA1MzA3MH0.W1zNngZCNzkO-eqNvD5DL4Sy1TludYM202Cm3IaHXFs
userEmail:r.amarante@gbh.com.do
userPassword:qwerty123
Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0MjQ6ODA5OTUyMTgxODozNTI2MzcwNzA2MDYwNjkiLCJleHAiOjE1Mjk1OTA5NTd9.3JajDBRcYt5lwcAIiv6Sb-rmM6X0WYEyJd9PsPz6tjY


# Generating Releases

Para hacer las distribuciones de la aplicación hay 2 formas.

> 1- Beta (por Crashlytics)

> 2- Store (por Google Play)

Todo el proceso de build está automatizado con Gradle. Si quieres ver todos los task existentes de gradle usa: `./gradlew task --all`

Para generar el APK y distribuirlo por Beta de Crashlytics sólo hay que correr el siguiente comando:
`./gradlew app:assembleNotEmulatedDeviceDevtBeta app:crashlyticsUploadDistributionNotEmulatedDeviceDevtBeta`

Si te fijas son dos tasks que se corren, la primera: app:assembleNotEmulatedDeviceDevtBeta genera el APK del variant Devt para dispositivos no emulados (físicos) usando la configuración Beta de build.

Y la segunda `app:crashlyticsUploadDistributionNotEmulatedDeviceDevtBeta` sube ese APK que generaste a Crashlytics.

Existe una variación de cada task por cada combinación de flavor/build type que exista en el build.gradle


# Errors
- `Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain` :

Verifica que tienes gradle instalado y luego corre 
> rm -rf gradle/ .gradle/ gradlew gradlew.bat

- `ERROR - Crashlytics Developer Tools error.
java.lang.IllegalArgumentException: Your fabric.properties file is missing your build secret.
Check the Crashlytics plugin to make sure that the application has been added successfully!
Contact support@fabric.io for assistance.`

En el proyecto, bajo app: 
> crea un archivo llamado `fabric.properties`

> busca las credenciales en fabric y agregalas como: 

```
apiSecret=changeMeToYourRealApiSecret
apiKey=changeMeToYourRealApiKey
```
*  build secret = api secret

tldr: Crea un release apuntando a development con distribución para el equipo de tpago:
> ./gradlew app:assembleNotEmulatedDeviceDevtBeta app:crashlyticsUploadDistributionNotEmulatedDeviceDevtBeta


`Nota: cada vez que realices un release, debes invitar al equipo de tpago o quien va a testear o no podrán descargar la nueva versión`


Para generar los releases para que el equipo de tpago suba al Store, solo corres
> `./gradlew app:assembleNotEmulatedDeviceProdStore`

Y le mandas el apk a la persona encargada de firmar(preguntar al equipo de tpago).

# Code Structure
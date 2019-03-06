GateIn API
=============

The GateIn Public API is to be used within [GateIn Portal](http://www.gatein.org) to provide a standard and consistent way to obtain and modify portal entities.


Getting Started
-----------

To build with maven

`mvn clean install`

and include as a dependency in your pom.xml

```xml
<groupId>org.gatein.api</groupId>
<artifactId>gatein-api</artifactId>
<version>1.0.0.Alpha01</version>
```

Usage
-----------

To obtain the `PortalRequest` object from within GateIn simply do the following:

```java
PortalRequest request = PortalRequest.getInstance();
```

The `PortalRequest` object contains information about the current request, for example to retrieve the current navigation node of the request:

```java
Navigation navigation = request.getNavigation();
Node node = navigaiton.getNode(request.getNodePath());
```

The `Portal` interface allows you to do more advanced things create sites, create pages, or check permissions. For example to check to see if the current user has access to a certain page:

```java
Portal portal = request.getPortal();
User user = request.getUser();
Page homepage = portal.getPage(new PageId("homepage"));
boolean access = portal.hasPermission(user, homepage.getAccessPermission());
```

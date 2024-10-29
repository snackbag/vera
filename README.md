# Vera
Vera is an interface-based UI library for Java, which allows for multiple render providers while the code stays the same.

```groovy
// build.gradle
dependencies {
    implementation "net.snackbag.vera:Vera:0.1.1"
}

repositories {
    maven { url = "https://artifacts.snackbag.net/artifactory/libs-release/" }
}
```

## Registering a provider
You can easily create your own provider. To register it, just do the following:
```java
import net.snackbag.vera.Vera;

Vera.registerProvider("<your provider name>", new YourProviderImplementation());
```

If you want to see a full provider implementation, check out [the Minecraft implementation](https://github.com/snackbag/mcvera)

## Creating a basic application
In this example, we'll show you how you can create a basic "Hello world" application.

```java
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.widget.VLabel;

public class ExampleApplication extends VeraApp {
    public ExampleApplication() {
        super(Vera.getProvider("<your render provider here>"));
    }
    
    // here is where the ui should be initialized
    @Override
    public void init() {
        VLabel label = new VLabel("Hello world!", this);
        addWidget(label);
    }
}
```
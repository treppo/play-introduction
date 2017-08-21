import com.google.inject.{AbstractModule, Provides}
import java.time.Clock

import github.{GithubClient, GithubGateway, HttpGithubClient, HttpGithubGateway}
import play.api.Configuration
import play.api.libs.ws.WSClient
import services._

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule {

  override def configure() = {
    // Use the system clock as the default implementation of Clock
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    // Ask Guice to create an instance of ApplicationTimer when the
    // application starts.
    bind(classOf[ApplicationTimer]).asEagerSingleton()
    // Set AtomicCounter as the implementation for Counter.
    bind(classOf[Counter]).to(classOf[AtomicCounter])
    bind(classOf[IpService]).to(classOf[ICanHazIpService])
    bind(classOf[GithubGateway]).to(classOf[HttpGithubGateway])
    bind(classOf[GithubClient]).to(classOf[HttpGithubClient])
  }

  @Provides
  def githubClient(wsClient: WSClient, configuration: Configuration): HttpGithubClient =
    new HttpGithubClient(wsClient,
      configuration.getString("githubApiUrl").getOrElse("https://api.github.com"))

}

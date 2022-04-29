package views.html
package analyse

import controllers.routes
import lila.api.{ Context, ExternalEngine }
import lila.app.templating.Environment._
import lila.app.ui.ScalatagsTemplate._

object external {
  def apply(prompt: ExternalEngine.Prompt)(implicit ctx: Context) = views.html.base.layout(
    title = "External engine",
    moreCss = cssTag("oauth"),
    moreJs = embedJsUnsafe(
      """setTimeout(() => lichess.load.then(() => {
  const btn = document.getElementById('engine-authorize');
  btn.removeAttribute('disabled');
  btn.setAttribute('class', 'button');
  btn.addEventListener('click', () => {
    lichess.storage.set('ceval.external', btn.getAttribute('data-ceval-external'));
    location.href = '/analysis';
  });
}), 2000);"""
    ),
  ) {
    main(cls := "oauth box box-pad")(
      div(cls := "oauth__top")(
        img(
          cls := "oauth__logo",
          alt := "linked rings icon",
          src := assetUrl("images/icons/linked-rings.png")
        ),
        h1("External engine"),
        strong(code(prompt.origin))
      ),
      prompt.insecure option flashMessage(cls := "flash-warning")(
        "Does not use a secure connection"
      ),
      p("Do you want to use this external engine on your device?"),
      form3.actions(
        a(href := routes.UserAnalysis.index)("Cancel"),
        button(cls := "button disabled", disabled := true, id := "engine-authorize")("Authorize")
      ),
      div(cls := "oauth__footer")(
        p("Not owned or operated by lichess.org"),
        p(cls := "oauth__redirect")(
          "Will connect to ",
          prompt.origin
        )
      )
    )
  }
}

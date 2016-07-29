(ns app2.templates.footer
  (:require
      [hiccup.page :refer [include-js]]
      [hiccup.element :refer [javascript-tag]]
      [optimus.html :as html]
    ))

  (defn template [opts]
    [:footer "\n\t"
      [:p "this is a paragraph in a footer!"] "\n\t"
      (include-js "js/main.js") "\n\t"
    ]
    ;; [:script "var _gaq=_gaq||[];_gaq.push(['_setAccount','UA-xxx-1']);_gaq.push(['_trackPageview']);(function(b){var c=b.createElement('script');c.type='text/javascript';c.async=true;c.src='http://www.google-analytics.com/ga.js';var a=b.getElementsByTagName('script')[0];a.parentNode.insertBefore(c,a)})(document);"]
    )

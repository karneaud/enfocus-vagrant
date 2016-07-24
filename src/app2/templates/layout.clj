(ns ^:figwheel-always app2.templates.layout
  (:require
        [hiccup.page :refer [html5]]
        [hiccup.def :refer [defelem]]
        [app2.templates.footer :as footer]
        [app2.templates.header :as header]
        [pl.danieljanus.tagsoup :as ts]
        ))

(defn template [content & opts]
  (html5
      (header/template opts) "\n\t"
      [:body "\n\t"
        [:div {:id "page" :class "w3-container" } "\n\t"
          (eval (read-string content)) "\n\t"
          (footer/template opts) "\n\t"
        ]
      ]
    ))

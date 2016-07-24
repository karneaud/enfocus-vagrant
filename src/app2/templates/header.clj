(ns app2.templates.header
  (:require
      [hiccup.page :refer [include-css include-js]]
      [hiccup.element]
      [optimus.html :as html]
    ))

  (defn template [opts]
    [:head "\n\t"
      [:meta {:charset "utf-8"}] "\n\t"
      [:meta {:name "viewport"
              :content "width=device-width, initial-scale=1.0"}] "\n\t"
      [:meta {:http-equiv "X-UA-Compatible" :content "chrome=1"}] "\n\t"
      [:meta {:http-equiv "X-UA-Compatible" :content "edge"}] "\n\t"
      [:title "NGC Race Alng the Piplines"] "\n\t"
      (include-css "http://www.w3schools.com/lib/w3.css") "\n\t"
      (include-css "/css/styles.css")  "\n\t"
    ])

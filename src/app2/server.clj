(ns app2.server
  (:use [ring.middleware.resource :only [wrap-resource]]
        [ring.middleware.file-info :only [wrap-file-info]]
        [ring.middleware.file :only [wrap-file]]
        [ring.middleware.reload :refer [wrap-reload]]
        [ring.middleware.content-type :refer [wrap-content-type]]
        [ring.middleware.refresh :refer [wrap-refresh]]
        [ring.util.response :refer [file-response]]
        [hiccup.page :refer [html5]])
  (:require
              [optimus.assets :as assets]
              [optimus.export]
              [optimus.optimizations :as optimizations]
              [optimus.prime :as optimus]
              [optimus.strategies :refer [serve-live-assets]]
              [stasis.core :as stasis]
              [app2.templates.layout :as layout]
              [clojure.string :as str]))

(defn wrap-utf-8
  "This function works around the fact that Ring simply chooses the default JVM
  encoding for the response encoding. This is not desirable, we always want to
  send UTF-8."
  [handler]
  (fn [request]
    (when-let [response (handler request)]
      (if (.contains (get-in response [:headers "Content-Type"]) ";")
        response
        (if (string? (:body response))
          (update-in response [:headers "Content-Type"] #(str % "; charset=utf-8"))
          response)))))

(defn get-assets []
  (concat (assets/load-bundle "public" "styles.css" [#"css/.+\.css$"])
    (assets/load-assets "public" [#"img/.*" "/data/questions.json" "/robots.txt"])
    (assets/load-bundle "public" "main.js" [#"js/.*"])
    ))

(defn layout-page [req page]
  (layout/template page)
  )

(defn hiccup-pages [pages]
  (zipmap (map #(str/replace % #"\/(\w+)\.hiccup$" "/$1.html") (keys pages))
          (map #(fn [req] (layout-page req %))(vals pages))))

(defn get-raw-pages []
  (stasis/merge-page-sources
   {:markdown (hiccup-pages (stasis/slurp-directory "resources/templates" #"[^.]+\.[^.]+"))}))

(defn get-pages []
  (stasis/merge-page-sources
   {:new-pages (get-raw-pages)
  }))

(defn handler [request]
            (file-response (:uri request) {:root "resources/public"}))

(def app (-> handler
             ;;(wrap-resource "resources/public")
             ;; wrap-file-info
             ;;(stasis/serve-pages get-pages)
             ;;(optimus/wrap get-assets optimizations/all serve-live-assets)
             wrap-content-type
             wrap-reload
             wrap-refresh
             wrap-utf-8))

(def export-dir "build")

(defn export-pages
  ([] (stasis/export-pages (get-pages) "resources/public" {}))
  ;;([assets] (stasis/export-pages (get-pages) "public" {:optimus-assets assets}))
  )

(defn export []
  (let [assets  (get-assets) ]
    (remove :outdated assets)
    (remove :bundled assets)
    (stasis/empty-directory! export-dir)
    (optimus.export/save-assets assets export-dir)
    (export-pages {:optimus-assets assets})
  ))

  ; (defn server
  ;   [req]
  ;   (run-server app {:join? false :port 3000}))

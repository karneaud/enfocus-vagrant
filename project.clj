(defproject app2 "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://exampl.com/FIXME"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.89"]
                 [ring "1.5.0"]
                 [stasis "2.2.0"]
                 [hiccup "1.0.5"]
                 [clj-tagsoup "0.3.0"]
                 [optimus "0.18.5"]
                 [ring/ring-jetty-adapter "1.5.0"]
                 [cljs-ajax "0.5.8"]
                 [enfocus "2.1.1"]]
  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-ring "0.9.7"]]
  :cljsbuild {:builds [

    {:id "dev"
    :incremental true
    :source-paths ["src/cljs"],
                        :figwheel {
                          :websocket-host :js-client-host
                          :on-jsload "scripts.dev/refresh"
                          :autoload false
                          :reload-dependents true
                          :debug true
                        }
                        :compiler {
                          :main "scripts.client"
                          :output-to "resources/public/js/main.js"
                          :output-dir "resources/public/js/out"
                          :asset-path "js/out"
                          ;;:pretty-print true
                          ;;:optimizations :none

                          }}]}
  :aliases {
    "start-dev" ["pdo" ["cljsbuild" "auto"] ["ring" "server-headless"]]
  }
  :figwheel {
    :css-dirs ["resources/public/css"]
    :reload-clj-files {:clj true :cljc false}
    :ring-handler app2.server/app
    :repl false
    :server-port 3000
  }
  :source-paths ["src"]
  :resource-paths ["resources"]
  :main app2.server
  :repl-options {
                :prompt (fn [ns] (str "your command for <" ns ">, master? " ))
                :welcome (println "Welcome to the magical world of the repl!")
                :init-ns app2.hawk
                :init (app2.hawk/init)
                :caught clj-stacktrace.repl/pst+
                :skip-default-init false
                :host "0.0.0.0"
                :port 9000
                :timeout 40000
              }
  :ring {
    :handler app2.server/app
    :auto-reload? true :auto-refresh? true :reload-paths ["resources/public"]
    :refresh-paths ["src/app2/templates"]}
  :profiles {
    :dev {
          :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
          :source-paths ["cljs_src"]
            :ring {
              :nrepl {
                :start? true
                :port 9000
                :host "0.0.0.0"
              }
            }
            :dependencies [
              [figwheel "0.5.4-7"]
              [com.stuartsierra/component "0.3.1"]
              [ring-refresh "0.1.1"]
              [http-kit "2.0.0"]
              [figwheel-sidecar "0.5.4-7"]
              [org.clojure/tools.nrepl "0.2.11"]
              [com.cemerick/piggieback "0.2.1"]
              [hawk "0.2.10"]
            ]
            :plugins [
              [lein-pdo "0.1.1"]
              [lein-figwheel "0.5.4-7"]
            ]
          }
        }

  )

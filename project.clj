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
  :main app2.server/app
  :ring {:handler app2.server/app :auto-reload? true :auto-refresh? true :reload-paths ["resources/public"]}
  :profiles {
    :dev {
            :ring {
              :nrepl {
                :start? true
                :port 9000
              }
            }
            :dependencies [
              [figwheel "0.5.4-7"]
            ]
            :plugins [
              [lein-pdo "0.1.1"]
              [lein-figwheel "0.5.4-7"]
            ]
          }
   }
  )

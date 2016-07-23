(defproject app2 "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://exampl.com/FIXME"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.89"]
                 [ring/ring-core "1.5.0"]
                 [ring/ring-jetty-adapter "1.5.0"]
                 [enfocus "2.0.0-SNAPSHOT"]]
  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-ring "0.9.7"]]
  :cljsbuild {:builds [{:source-paths ["src/cljs"],
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
  :main app2.server/app
  :ring {:handler app2.server/app :auto-reload? true :auto-refresh? true :reload-paths ["src" "resources"]}
  :profiles {
    :dev {
            :ring {
              :nrepl {
                :start? true
                :port 9000
              }
            }
            :plugins [
              [lein-pdo "0.1.1"]
            ]
          }
   }
  )

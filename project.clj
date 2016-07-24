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
    "start-dev" ["pdo" ["cljsbuild" "auto"] ["run" "-m" "app2.server/app"]]
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
            ]
            :plugins [
              [lein-pdo "0.1.1"]
              [lein-figwheel "0.5.4-7"]
            ]
          }
   }
   :repl-options {
                 :prompt (fn [ns] (str "your command for <" ns ">, master? " ))
                 ;; What to print when the repl session starts.
                 :welcome (println "Welcome to the magical world of the repl!")
                 ;; Specify the ns to start the REPL in (overrides :main in
                 ;; this case only)
                 ;;:init-ns foo.bar
                 ;; This expression will run when first opening a REPL, in the
                 ;; namespace from :init-ns or :main if specified.
                 :init (require '[app2.figwheel])
                 ;; Print stack traces on exceptions (highly recommended, but
                 ;; currently overwrites *1, *2, etc).
                 :caught clj-stacktrace.repl/pst+
                 ;; Skip's the default requires and printed help message.
                 :skip-default-init false
                 ;; Customize the socket the repl task listens on and
                 ;; attaches to.
                 :host "0.0.0.0"
                 :port 9000
                 ;; If nREPL takes too long to load it may timeout,
                 ;; increase this to wait longer before timing out.
                 ;; Defaults to 30000 (30 seconds)
                 :timeout 40000
                 ;;:verbose true
                 ;; nREPL server customization
                 ;; Only one of #{:nrepl-handler :nrepl-middleware}
                 ;; may be used at a time.
                 ;; Use a different server-side nREPL handler.
                ;; :nrepl-handler (clojure.tools.nrepl.server/default-handler)
                 ;; Add server-side middleware to nREPL stack.
                ;  :nrepl-middleware [my.nrepl.thing/wrap-amazingness
                ;                     ;; TODO: link to more detailed documentation.
                ;                     ;; Middleware without appropriate metadata
                ;                     ;; (see clojure.tools.nrepl.middleware/set-descriptor!
                ;                     ;; for details) will simply be appended to the stack
                ;                     ;; of middleware (rather than ordered based on its
                ;                     ;; expectations and requirements).
                ;                     (fn [handler]
                ;                       (fn [& args]
                ;                         (prn :middle args)
                ;                         (apply handler args)))]
                                      }
  )

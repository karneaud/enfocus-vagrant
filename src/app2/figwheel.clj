(require
 '[figwheel-sidecar.components.figwheel-server :as server]
 '[figwheel-sidecar.components.file-system-watcher :as fsw]
 '[figwheel-sidecar.system :as sys]
 '[figwheel-sidecar.utils :as utils]
 '[com.stuartsierra.component :as component]
 '[app2.server :as handler])

(def figwheel-config
  {
  :figwheel-options {
    :css-dirs ["public/css"]
    :reload-clj-files {:clj true :cljc false}
    :ring-handler handler/app
    ;:repl true
    :server-port 3000
    ;;:verbose true
  }
   :build-ids ["dev"]
   :all-builds
   [{:id "dev"
     :figwheel {
       :websocket-host :js-client-host
       :on-jsload "scripts.dev/refresh"
       :autoload false
       :reload-dependents false
       :incremental true
       :debug true
     }
     :source-paths ["src/cljs"]
     :compiler {
       :main "scripts.dev"
       :output-to "resources/public/js/main.js"
       :output-dir "resources/public/js/out"
       :asset-path "js/out"
       ;;:pretty-print true
       ;;:optimizations :none

       }}]})

(defn make-file [path]
  {:file (utils/remove-root-path path)
   :type :html})

(defn send-files [figwheel-server files]
  (server/send-message figwheel-server
                       ::server/broadcast
                       {:msg-name :html-files-changed
                        :files files}))

(defn handle-notification [watcher files]
  (println  "filter" (filter #(re-matches #".*\.(clj|hiccup|css)$" %) (map str files)))
  (when-let [changed-files (not-empty (filter #(re-matches #".*\.(clj|hiccup|css)$" %) (map str files)))]
    (let [figwheel-server (:figwheel-server watcher)
          sendable-files (map #(make-file %) changed-files)]
      (send-files figwheel-server sendable-files)
      (doseq [f sendable-files]
        (println "sending changed HTML file:" (:file f))))))

(def system
  (atom
   (component/system-map
    :figwheel-server (sys/figwheel-system figwheel-config)
    :html-watcher (component/using
                   (fsw/file-system-watcher {:watcher-name "HTML watcher"
                                             :watch-paths ["resources/templates" "src/app2/templates" "resources/public/css"]
                                             :notification-handler handle-notification})
                   [:figwheel-server]))))

(defn start []
  (swap! system component/start))

(defn stop []
  (swap! system component/stop))

(defn reload []
  (stop)
  (start))

(defn repl []
  (sys/cljs-repl (:figwheel-server @system)))

;; Start the components and the repl
(start)
;; (repl)

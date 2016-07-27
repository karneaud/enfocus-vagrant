(ns app2.hawk
(:require [hawk.core :as hawk])
(:require [app2.server :refer [export-pages]]))

(defn init
  []
  (export-pages)
  (hawk/watch! [{:paths ["src/app2/templates" "resources/templates"]
               :filter hawk/modified?
               :handler (fn [ctx e]
                          (export-pages)
                          )}]))

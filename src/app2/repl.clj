(require '[cljs.repl :as repl])
(require '[cljs.repl.rhino :as rhino])
(require '[cljs.build.api :as cljsc])

(def env (rhino/repl-env))
(repl/repl env)

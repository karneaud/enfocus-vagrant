(ns scripts.dev)

(defn refresh []
  (set! (.-href (.-location js/window))
        (.-href (.-location js/window))))

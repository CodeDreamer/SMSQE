; Get menu object (entry) of a menu     1992 Jochen Merz

        section utility

        include dev8_keys_wwork

        xdef    ut_gtmobe       ; get menu object entry
        xdef    ut_gtmobj       ; get menu object

;+++
; Get a menu object entry or menu object of a standard menu sub-window.
;
;               Entry                   Exit
;       d0                              smashed
;       d1      virtual row|column      preserved
;       a0                              preserved
;       a1                              object or object entry
;       a2                              preserved
;       a3      sub-window defn         preserved
;       a4                              preserved
;---
ut_gtmobj
        bsr.s   ut_gtmobe               ; get entry
        move.l  wwm_pobj(a1),a1         ; get object
        rts

ut_gtmobe
        move.l  wwa_rowl(a3),a1         ; item row list
        move.w  d1,d0
        mulu    #wwm.rlen,d0            ; index into row list
        move.l  (a1,d0.l),a1            ; that's the row
        move.l  d1,d0
        swap    d0
        mulu    #wwm.olen,d0            ; now index to object
        add.l   d0,a1                   ; that's it!
        rts

        end

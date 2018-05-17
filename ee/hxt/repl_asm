; Replace item      V2.00     1990   Tony Tebby   QJUMP

        section exten

        xdef    hxt_repl

        xref    hk_fitmc
        xref    hk_remvc

        xref    gu_hkuse
        xref    gu_hkfre

        include 'dev8_keys_err'
        include 'dev8_ee_hk_data'

;+++
; Replace item.
; Removes item if it exists, and if it is the same type of item.
;
;       d1  s
;       d6 c  p (word) item type
;       a1 c  p pointer to HOTKEY char parameter
;       a3  s
;       status return 0 or err.fdiu
;---
hxt_repl
        move.l  a1,-(sp)
        move.l  (a1),d1                  ; HOTKEY
        jsr     gu_hkuse                 ; use hotkey
        bne.s   hrp_exit

        jsr     hk_fitmc                 ; find it
        bne.s   hrp_ok
        cmp.w   hki_type(a1),d6          ; same type
        bne.s   hrp_fdiu                 ; ... no
        jsr     hk_remvc
        bra.s   hrp_free

hrp_fdiu
        moveq   #err.fdiu,d0
        bra.s   hrp_free
hrp_ok
        moveq   #0,d0
hrp_free
        jsr     gu_hkfre                 ; free the hotkey system

hrp_exit
        move.l  (sp)+,a1
        rts
        end

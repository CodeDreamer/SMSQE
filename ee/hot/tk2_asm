; dummy TK2_EXT      V2.00    1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_tk2
        xdef    hot_tkext

        xref    hot_thus
        xref    hot_thfr
        xref    hot_ptk2
        xref    ut_reassert

        include 'dev8_ee_hk_data'

;+++ 
; This routine calls the normal TK2_EXT (address saved in thing) and
; then re-asserts its own ALTKEY
;---
hot_tk2
        move.l  a3,a4                    ; find hot
        jsr     hot_thus
        bne.s   tk2_rts                  ; ... oops
        move.l  hkd_tk2x(a3),a1          ; real tk2_ext routine
        jsr     hot_thfr
        move.l  a4,a3                    ; restore parameter pointer
        move.l  a1,d0                    ; any tk2_ext?
        beq.s   tk2_rts                  ; ... no
        jsr     (a1)                     ; do it

hot_tkext
        lea     hot_ptk2,a1              ; use our TK2 versions
        jsr     ut_reassert
tk2_rts
        rts
        end

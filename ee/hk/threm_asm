; HOTKEY remove routine  V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_threm

        xref    hk_kjob
        xref    hk_remv
        xref    gu_rchp

        include 'dev8_keys_qdos_sms'
        include 'dev8_ee_hk_data'
;+++
; Remove routine for Hotkey Thing
;---
hk_threm
        move.l  a1,a4                    ; save thing linkage
        lea     hkd.thg(a1),a3           ; real linkage
        lea     hkd_pllk(a3),a0          ; unlink polling routine
        moveq   #sms.rpol,d0
        trap    #1

        move.l  hkd_bbas(a3),d0          ; stuffer buffer
        beq.s   hkc_rkey                 ; ... none
        move.l  d0,a0
        jsr     gu_rchp

hkc_rkey
        move.w  #$00ff,d1                ; key
        sub.l   a1,a1                    ; ... not string
hkc_rklp
        jsr     hk_remv                  ; remove
        subq.w  #1,d1                    ; ... next one
        bge.s   hkc_rklp

        jsr     hk_kjob                  ; kill hotkey Job

        move.l  a4,a0                    ; and remove heap item
        jmp     gu_rchp
        end

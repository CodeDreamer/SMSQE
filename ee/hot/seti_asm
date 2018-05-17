; Routine to set HOTKEY item  V2.02     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_seti
        xdef    hot_sets

        xref    hot_gtky
        xref    hot_thact
        xref    hot_rter
        xref    hk_newst
        xref    ut_gxnm1
        xref    ut_gtnm1
        xref    gu_achpp
        xref    gu_rchp

        include 'dev8_keys_err'
        include 'dev8_ee_hk_data'

;+++
; Set simple hotkey item, and return to basic
;
;       d6 c s  hotkey item type
;---
hot_seti
        jsr     hot_gtky                 ; first the key
        bne.s   hs_rts

        jsr     ut_gxnm1                 ; get second string
        bne.s   hs_rts

;+++
; Set simple hotkey item, and return to basic
;
;       d6 c s  hotkey item type
;       d7 c s  Hotkey
;       a1 c  p pointer to item name string (a6,a1.l)
;---
hot_sets
        move.w  (a6,a1.l),d1             ; length of name
        moveq   #hki_name+2,d0           ; allocate space for item
        add.w   d1,d0
        jsr     gu_achpp
        bne.s   hs_rter
        move.l  a1,a2                    ; name pointer
        move.l  a0,a1                    ; item base
        move.w  #hki.id,(a0)+            ; ID
        move.w  d6,(a0)+                 ; and type
        clr.l   (a0)+                    ; pointer
        move.w  d1,(a0)+                 ; name length
hs_nmloop
        move.b  2(a6,a2.l),(a0)+         ; and the rest of the name
        addq.l  #1,a2
        subq.w  #1,d1
        bgt.s   hs_nmloop

        lea     hk_newst,a2              ; set new hotkey
        move.w  d7,d1                    ; key
        jsr     hot_thact                ; action
        beq.s   hs_rter
        move.l  a1,a0                    ; remove item
        jsr     gu_rchp
hs_rter
        jmp     hot_rter

hs_rts
        rts
        end

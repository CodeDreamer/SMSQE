; Routine to set HOTKEY LOAD item  V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_ldit

        xref    hk_sprc

        include 'dev8_ee_hk_data'

;+++
; Set HOTKEY LOAD item
;
;       d1 c  p (word) grabber memory (0 none, -ve ask)
;       d2 c  p guardian window size (0 none, -ve unlock)
;       d3 c  p guardian window origin
;       d4 c  p (word) offset to parameter item name
;       d5 c  p (word) offset to wake / program name in item name
;       d6 c  p (word) load / wake action
;       d7 c  p (byte) HOTKEY
;       a0 c  p pointer to item
;       a1 c  p pointer to item name (rel a6)
;
;       status return ok
;---
hot_ldit
reglist reg     d5/a0/a2/a3
        movem.l reglist,-(sp)
        move.w  #hki.id,(a0)+            ; set id
        move.w  d6,(a0)+                 ; type of action
        addq.l  #4,a0                    ; pointer not set

        bsr.s   hld_cnam                 ; copy filename

        jsr     hk_sprc

        moveq   #0,d0
        movem.l (sp)+,reglist
        rts

hld_cnam
        move.l  a1,a3                    ; start of filename
        move.w  (a6,a3.l),d0             ; get length
        addq.l  #2,a3
hld_copy
        move.w  d0,(a0)+                 ; length of name
        beq.s   hld_rts
hld_cnloop
        move.b  (a6,a3.l),(a0)+
        move.b  1(a6,a3.l),(a0)+         ; in words to keep alignment
        addq.l  #2,a3
        subq.w  #2,d0
        bgt.s   hld_cnloop
hld_rts
        rts
        end

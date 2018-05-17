; Procedure to set up CHAR hotkeys  V2.00     1990   Tony Tebby

        section hotkey

        xdef    hxt_char
        xdef    hxt_cmd

        xref    hxt_pick

        xref    hxt_prms
        xref    hxt_repl
        xref    hxt_mkit
        xref    hxt_adit

        xref    gu_hkuse
        xref    gu_hkfre

        include 'dev8_ee_hk_data'
        include 'dev8_keys_thg'
        include 'dev8_keys_k'
        include 'dev8_mac_thg'

;+++
; HOT_CHAR key, string, string, .....
;---
hxt_char thg_extn CHAR,hxt_cmd,hxt_prms

hxc.reg reg     d1/d6/a0/a1/a2/a3
        movem.l hxc.reg,-(sp)
        moveq   #hki.stuf,d6
        bra.s   hxc_do

;+++
; HOT_CMD key, string
;---
hxt_cmd thg_extn {CMD },hxt_pick,hxt_prms

        movem.l hxc.reg,-(sp)
        moveq   #hki.cmd,d6
hxc_do
        jsr     hxt_repl                 ; call to replace
        bne.s   hxc_exit

        lea     4(a1),a2                 ; parameter list
        move.l  (a2)+,d0                 ; number of strings
        beq.s   hxc_exit                 ; ... none, done
        moveq   #0,d1

hxc_cntl
        addq.l  #4,a2
        move.l  (a2)+,a3                 ; first string
        addq.w  #1,d1
        add.w   (a3),d1                  ; total length
        subq.w  #1,d0
        bgt.s   hxc_cntl

        jsr     hxt_mkit                 ; allocate and make item
        bne.s   hxc_exit

        move.l  a2,hki_ptr(a0)           ; set pointer

        subq.w  #1,-2(a2)                ; adjust length of string

        move.l  (a1)+,d1                 ; key

        move.l  (a1)+,d0                 ; number of strings
hxc_cpyl
        addq.l  #4,a1
        move.l  (a1)+,a3                 ; first string
        move.w  (a3)+,d6                 ; length
        bra.s   hxc_chre
hxc_chrl
        move.b  (a3)+,(a2)+              ; characters
hxc_chre
        dbra    d6,hxc_chrl

        move.b  #k.nl,(a2)+              ; and new line

        subq.w  #1,d0
        bgt.s   hxc_cpyl

        clr.b   -(a2)                    ; null at end rather than NL

        jsr     hxt_adit                 ; add item

hxc_exit
        movem.l (sp)+,hxc.reg
        rts

        end

; Routine to read standard Hotkey params    V2.01    1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_parm
        xdef    hot_park

        xref    hot_gtky
        xref    hot_itnm
        xref    ut_gtnm1
        xref    ut_gxin1
        xref    ut_gtint
        xref    cv_locas

        include 'dev8_keys_err'
        include 'dev8_ee_hot_bv'
        include 'dev8_ee_hk_data'

hpm_scr dc.w    $200,$100                ; screen size
hpm.psib equ    0
;+++
; Read Hotkeyed program parameters starting with the Hotkey
;
;       d1  r   grabber memory allocation
;       d2  r   guardian window size
;       d3  r   guardian window origin
;       d4  r   (word) start of substring with item name
;       d5  r   (word) length of substring with item name
;       d6 cr   lsbyte set if impure, clear if pure; msword unmodified
;       d7  r   (word) hotkey 
;       a1  r   (bv_bfbas) pointer to filename / item name
;       a2   s
;       a3 cu   pointer to first parameter
;       a5 c  p pointer to last parameter
;       status returns standard
;---
hot_park
frame   equ     $c
stk_frez equ    $0
stk_meml equ    $2
stk_wdef equ    $4
        jsr     hot_gtky                 ; get key
        bne.l   hpm_rts                  ; ... oops
;+++
; Read Hotkeyed program parameters no Hotkey
;
;       d1  r   msw -ve if freeze / lsw grabber memory allocation
;       d2  r   guardian window size (zero none, negative if unlock)
;       d3  r   guardian window origin
;       d4  r   (word) start of substring with item name
;       d5  r   (word) length of substring with item name
;       d6 cr   lsbyte set if impure, clear if pure; msword unmodified
;       a1  r   (bv_bfbas) pointer to filename / item name
;       a2   s
;       a3 cu   pointer to first parameter
;       a5 c  p pointer to last parameter
;       status returns standard
;---
hot_parm
        clr.l   -(sp)
        clr.l   -(sp)
        clr.l   -(sp)
        jsr     hot_itnm                 ; get complete item name
        sf      d6
        blt.l   hpm_exit                 ; ... bad
        bgt.s   hpm_chko                 ; ... option character

hpm_gtopt
        cmp.l   a3,a5
        beq.l   hpm_ok                   ; ... done

        jsr     ut_gtnm1                 ; next item
        bne.l   hpm_exit
        cmp.w   #1,(a6,a1.l)             ; one character
        bne.l   hpm_ipar
        addq.l  #8,a3                    ; next param

        move.b  2(a6,a1.l),d1            ; option
hpm_chko
        jsr     cv_locas                 ; lower case
        cmp.b   #'i',d1                  ; impure?
        bne.s   hpm_ckopt
        st      d6                       ; flag it
        bra.s   hpm_gtopt                ; and get another option

hpm_ckopt
        cmp.b   #'p',d1                  ; Psion?
        beq.s   hpm_psion                ; ... yes
        cmp.b   #'g',d1                  ; guardian?
        beq.s   hpm_guard                ; ... yes
        cmp.b   #'f',d1                  ; freeze
        seq     stk_frez(sp)             ; set if it is
        beq.s   hpm_guard                ; ... yes!!
        cmp.l   a5,a3                    ; more parameters?
        bne.l   hpm_ipar                 ; ... yes!!
        cmp.b   #'u',d1                  ; unlock?
        bne.l   hpm_ipar                 ; ... none of these

hpm_unlock
        not.l   stk_wdef(sp)             ; ... unlock
        bra.l   hpm_ok

hpm_guard
        move.l  hpm_scr,stk_wdef(sp)     ; set full screen guardian
        cmp.l   a5,a3                    ; any parameters?
        bge.l   hpm_ok                   ; ... no
        jsr     ut_gtint                 ; get integers
        bne.s   hpm_exit
        cmp.w   #4,d3                    ; four?
        bne.s   hpm_ipar                 ; ... no
        move.l  (a6,a1.l),stk_wdef(sp)   ; set definition
        move.l  4(a6,a1.l),stk_wdef+4(sp)
        addq.l  #8,a1
        bra.s   hpm_setri

hpm_psion
        not.w   stk_meml(sp)             ; flag ask for space
        move.l  hpm_scr,stk_wdef(sp)     ; big guardian
        tst.w   d5                       ; any program name?
        bne.s   hpm_grab                 ; ... yes, find grabber

        move.l  bv_bfbas(a6),a1          ; base of buffer
        addq.l  #2,a1
        move.l  a1,a0                    ; start of file name characters
        move.w  -2(a6,a0.l),d5           ; end of filename / start of Job name
        add.w   d5,a1
        moveq   #1,d0                    ; include separator

hpm_pnloop
        cmp.b   #'_',-1(a6,a1.l)         ; '_'?
        beq.s   hpm_pnset                ; ... yes
        cmp.l   a0,a1                    ; back to start?
        beq.s   hpm_pnset                ; ... yes
        addq.w  #1,d0                    ; ... no, another character
        subq.l  #1,a1
        bra.s   hpm_pnloop

hpm_pnset
        add.w   d0,-2(a6,a0.l)           ; item is longer
        subq.w  #1,d0
        lea     (a1,d0.w),a0             ; end of filename
        move.b  #',',(a6,a0.l)           ; separator
        bra.s   hpm_splend
hpm_sploop
        addq.l  #1,a0
        move.b  (a6,a1.l),(a6,a0.l)
        addq.l  #1,a1
hpm_splend
        dbra    d0,hpm_sploop

hpm_grab
        cmp.l   a5,a3                    ; another parameter?
        bge.s   hpm_ok                   ; ... no
        jsr     ut_gxin1                 ; ... yes, one integer
        bne.s   hpm_exit
        move.w  (a6,a1.l),stk_meml(sp)   ; put on stack
        addq.l  #4,a1
hpm_setri
        move.l  a1,bv_rip(a6)            ; clean up RI
hpm_ok
        moveq   #0,d0
hpm_exit
        move.l  bv_bfbas(a6),a1          ; point to name(s)
        movem.l (sp)+,d1/d2/d3           ; set guardian parameters
hpm_rts
        rts

hpm_ipar
        moveq   #err.ipar,d0
        bra.s   hpm_exit
        end

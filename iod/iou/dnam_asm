; Check device name   V2.01    1987  Tony Tebby   QJUMP

        section iou

        xdef    iou_dnam        ; check name

        xref    iou_ckch        ; check characters
        xref    cv_upcas
        xref    cv_ctype
        xref    cv_deciw        ; decimal conversion

        include 'dev8_keys_err'
        include 'dev8_keys_k'
;+++
; Check device name
;
;       d0  r   0, err.fdnf or err.inam
;       a0 c  p pointer to name
;       a1   sp pointer to name definition  (6 on from return address)
;       a3 c  p pointer to parameter block
;
;       all other registers preserved
;       standard error return
;
;       multiple returns
;       normal - not found
;       +2     - invalid name
;       +4     - ok
;---
iou_dnam
reglist reg     d1/d2/d3/a0/a3/a4
ret_add equ     $18
name_def equ    $06

        movem.l reglist,-(sp)            ; save registers
        move.l  ret_add(sp),a4           ; get return address
        addq.l  #name_def,a4             ; ... hence name definition

        move.w  (a4)+,d0                 ; length of device name
        moveq   #0,d7
        move.w  (a0)+,d7                 ; length of name to check
        cmp.w   d0,d7                    ; long enough?
        blo.l   idn_fdnf                 ; ... no
        add.l   a0,d7                    ; end of name

        bsr.l   iou_ckch                 ; matches?
        bne.l   idn_fdnf

        move.l  a4,d0                    ; re-align definition on word boundary
        addq.l  #1,d0
        bclr    #0,d0
        move.l  d0,a4

        move.w  (a4)+,d3                 ; number of parameters
        bra.s   idn_plend 

idn_ploop
        move.b  (a4)+,d1                 ; get type of parameter
        bgt.s   idn_sep                  ; ... separator
        blt.s   idn_nsep                 ; ... no separator

        moveq   #0,d0
        move.b  (a4)+,d0                 ; number of code bytes
        cmp.w   a0,d7                    ; any name left?
        beq.s   idn_nparm

        move.b  (a0),d1                  ; next character
        jsr     cv_upcas                 ; ... upper cased
        move.b  d1,d2
        move.w  d0,d1
        bra.s   idn_cdlend
idn_cdlp
        cmp.b   (a4,d1.w),d2             ; code matches?
        beq.s   idn_codf                 ; ... yes
idn_cdlend
        dbeq    d1,idn_cdlp              ; fall out if no parm or loop exhausted
idn_nparm
        moveq   #0,d1                    ; no parm
        bra.s   idn_cdset

idn_codf
        addq.l  #1,a0                    ; remove code found
        addq.w  #1,d1                    ; adjust code to 1 to n

idn_cdset
        addq.w  #1,d0                    ; align to word
        bclr    #0,d0
        add.w   d0,a4                    ; and update definition
        bra.s   idn_pset

; check for separator

idn_sep
        cmp.l   d7,a0                    ; end?
        bge.s   idn_nsep                 ; ... yes
        moveq   #1,d0
        bsr.l   iou_ckch                 ; separator matches?
        beq.s   idn_fval                 ; ... yes
        subq.w  #1,a0                    ; ... no, backspace
        bra.s   idn_nval

idn_nsep
        addq.l  #1,a4                    ; skip sep

idn_fval
        cmp.l   a0,d7                    ; end of buffer?
        beq.s   idn_nval                 ; ... yes
        move.b  (a0),d1                  ; next character
        jsr     cv_ctype                 ; type
        subq.b  #k.dig09,d1              ; digit?
        bne.s   idn_nval                 ; ... no
        jsr     cv_deciw
        beq.s   idn_vset                 ; ... ok, set it
        bra.s   idn_inam                 ; ... bad, invalid name

idn_nval
        move.w  (a4),d1                  ; get default

idn_vset
        addq.w  #2,a4                    ; move to next bit of defn
idn_pset
        move.w  d1,(a3)+                 ; set parameter
idn_plend
        dbra    d3,idn_ploop

        cmp.l   a0,d7                    ; should b at end
        bne.s   idn_inam
idn_ok
        addq.l  #4,ret_add(sp)           ; set return address ok
        moveq   #0,d0                    ; ok
        bra.s   idn_exit
idn_inam
        addq.l  #2,ret_add(sp)
        moveq   #err.inam,d0             ; invalid name
        bra.s   idn_exit
idn_fdnf
        moveq   #err.fdnf,d0             ; not found
idn_exit
        movem.l (sp)+,reglist
        rts
        end

; Make a list out of a string array

        section utility

        xdef    ut_mkl2st

        include dev8_keys_err
        include dev8_keys_bv

        xref    gu_mklis

;+++
; Make a list out of a string array passed in the standard parameter form.
; This routine checks for the -ve SuperBASIC flag. The array has to be in
; a 2-dimensional form.
;
;               Entry                           Exit
;       D1.w                                    number of entries
;       A1      ptr to parameter entry          ptr to list or 0
;
;       Error returns:  ipar    not 2-dimensional
;---
ut_mkl2st
        movem.l d2-d3/a0/a2-a3/a5,-(sp)
        moveq   #err.ipar,d0            ; maybe wrong nr of dimensions
        move.l  4(a1),a2                ; pointer to array descriptor
        move.w  2(a1),d1                ; -ve if SuperBASIC
        bge.s   mk2s_nobas
        move.l  bv_vvbas(a6),a3         ; variable table
        add.l   a2,a3
        cmp.w   #2,4(a6,a3.l)           ; two dimensions?
        bne.s   mk2s_ret
        moveq   #7,d0
        add.w   10(a6,a3.l),d0          ; item length
        bclr    #0,d0                   ; make even
        lea     mk_2dsbas,a0            ; entry generating code
        move.l  (a6,a3.l),a2            ; pointer to array contents
        add.l   bv_vvbas(a6),a2         ; now relative
        bsr     gu_mklis                ; create list
        bra.s   mk2s_ret
mk2s_nobas
        cmp.w   #2,4(a2)                ; two dimensions?
        bne.s   mk2s_ret
        moveq   #7,d0
        add.w   10(a2),d0               ; item length
        bclr    #0,d0                   ; make even
        lea     mk_2dsjob,a0            ; create list
        move.l  (a2),a3                 ; pointer to entry table
        tst.w   d1                      ; absolute pointer?
        beq.s   nobas_abs
nobas_rel
        add.l   a2,a3                   ; make relative pointer absolute
nobas_abs
        bsr     gu_mklis
mk2s_ret
        movem.l (sp)+,d2-d3/a0/a2-a3/a5
        rts

;+++
;               Entry                   Exit
;       d3      entry number            preserved
;       a1      entry                   preserved
;       a2      contents (a6)
;       a3      descriptor (a6)
;---
mk_2dsbas
        movem.l a1-a3,-(sp)
        cmp.w   6(a6,a3.l),d3           ; out of range?
        bhi.s   bas_eof
        moveq   #0,d0
        move.w  d3,d1
        mulu    8(a6,a3.l),d1           ; index right item
        lea     4(a1),a1                ; base of item in table
        add.l   d1,a2                   ; point to item
        move.w  (a6,a2.l),d1            ; string length
        addq.l  #2,a2
        move.w  d1,(a1)+                ; set length
bas_loop
        beq.s   bas_ret                 ; string set complete
        move.b  (a6,a2.l),(a1)+
        addq.l  #1,a2                   ; copy whole string
        subq.w  #1,d1                   ; count down
        bra.s   bas_loop
bas_eof
        moveq   #err.eof,d0
bas_ret
        movem.l (sp)+,a1-a3
        rts

;+++
;               Entry                   Exit
;       d3      entry number            preserved
;       a1      entry                   preserved
;       a2      descriptor
;       a3      contents
;---

mk_2dsjob
        movem.l a1-a3,-(sp)
        cmp.w   6(a2),d3                ; out of range?
        bhi.s   bas_eof
        moveq   #0,d0
        move.w  d3,d1
        mulu    8(a2),d1                ; index right item
        lea     4(a1),a1                ; base of item in entry
        add.l   d1,a3                   ; item in array
        move.w  (a3)+,d1                ; string length
        move.w  d1,(a1)+                ; set length
job_loop
        beq.s   bas_ret                 ; all set
        move.b  (a3)+,(a1)+             ; copy character
        subq.w  #1,d1                   ; count down
        bra.s   job_loop

        end

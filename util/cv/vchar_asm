; Make a visible form of any character  V0.00    1988 Tony Tebby  QJUMP

        section cv

        xdef    cv_vchar
        xdef    cv_vchrs

        xref    cv_vctrl        ; chars 0 to $1f
        xref    cv_vcurs        ; cursor / caps $c0 to $e7
        xref    cv_vfunc        ; function $e8 to $fb
        xref    cv_vodds        ; odds $fc to $ff
        xref    cv_vcacs        ; alt ctrl shift

        xref    cv_cttab

        include 'dev8_keys_k'

;+++
; Convert the character in d1 to a visible string (a1). If the character is
; already visible, the routine returns status 0, otherwise the status and d0
; are set positive. A1 must point to an area large enough to accomodate the
; visible string (8 bytes for the default strings)
;
; SPACE is treated as a special non-printable character which will use the
; string pointer to by A2
;
;       d1 c  p character
;       a1 c  p pointer to visible string
;       a2 c  p pointer to string for 'SPACE'
;       status return not standard
;---
cv_vchrs
reglist reg     d1/a1/a2
        movem.l reglist,-(sp) 
        cmp.b   #' ',d1          ; SPACE?
        bne.s   ucv_look
        move.w  (a2)+,d0
        move.w  d0,(a1)+
ucv_sloop
        move.b  (a2)+,(a1)+
        subq.w  #1,d0
        bgt.s   ucv_sloop
        bra.s   ucv_sset

;+++
; Convert the character in d1 to a visible string (a1). If the character is
; already visible, the routine returns status 0, otherwise the status and d0
; are set positive. A1 must point to an area large enough to accomodate the
; visible string (8 bytes for the default strings)
;
;       d1 c  p character
;       a1 c  p pointer to visible string
;       status return not standard
;---
cv_vchar
        movem.l reglist,-(sp)
ucv_look
        clr.w   (a1)                     ; assume printable character
        moveq   #0,d0
        move.b  d1,d0
        lea     cv_cttab,a2
        tst.b   (a2,d0.w)                ; is character printable?
        bgt.s   ucv_ok
        tst.b   d0                       ; what type of odd
        bge.s   ucv_ctrl                 ; control char
        sub.b   #k.curs,d0               ; cursor (and caps) 0 to $28
        move.b  d0,d1
        sub.b   #k.f1-k.curs,d0          ; is it?
        blt.s   ucv_curs                 ; no
        move.b  d0,d1
        sub.b   #k.csf5+1-k.f1,d0
        blt.s   ucv_funct

        lea     cv_vodds,a2              ; use odds table
        bra.s   ucv_add1

ucv_funct
        pea     cv_vfunc                 ; function keys
        add.b   d1,d1                    ; alt never set
        bra.s   ucv_add2
        
ucv_curs
        pea     cv_vcurs                 ; cursor and caps
ucv_add2
        moveq   #%111,d0                 ; shift ctrl and alt
        and.b   d1,d0
        lsr.b   #3,d1

        lea     cv_vcacs,a2              ; add control alt shift
        bsr.s   ucv_add

        move.b  d1,d0
        move.l  (sp)+,a2                 ; and other bit
        bra.s   ucv_add1

ucv_ctrl
        lea     cv_vctrl,a2              ; use control table
ucv_add1
        bsr.s   ucv_add
ucv_sset
        moveq   #1,d0
        bra.s   ucv_exit

ucv_ok
        addq.w  #1,(a1)+                 ; set single byte string
        move.b  d1,(a1)
        moveq   #0,d0
ucv_exit
        movem.l (sp)+,reglist
        rts

ucv_add
        move.l  a1,-(sp)                 ; save a1
        add.w   d0,a2                    ; index table
        move.b  (a2),d0
        add.w   d0,a2                    ; indirect
        move.b  (a2)+,d0                 ; ... length
        add.b   d0,1(a1)                 ; ... new length
        add.w   (a1)+,a1                 ; end
        sub.w   d0,a1                    ; ... no, middle
ucv_loop
        move.b  (a2)+,(a1)+              ; copy characters
        subq.b  #1,d0
        bgt.s   ucv_loop

        move.l  (sp)+,a1
        rts
        end

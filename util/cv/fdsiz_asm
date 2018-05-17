; Formatted date size   V2.00           1988   Tony Tebby   QJUMP

        section cv

        xdef    cv_fdsiz

        xref    cv_fditb
        xref    cv_fdstb

        include 'dev8_keys_err'
        include 'dev8_keys_k'
        include 'dev8_keys_qlv'
        include 'dev8_mac_assert'

;+++
; Find formatted date size
;
;       d1  r   number of columns
;       d2  r   number of rows
;       a2 c  p pointer to format string
;       status return zero or err.nc
;---
cv_fdsiz
reg.fdsz reg    d3/d4/d5/a0/a2
        movem.l reg.fdsz,-(sp)
        moveq   #0,d1                    ; line length
        moveq   #1,d2                    ; number of lines
        moveq   #0,d5                    ; longest line so far
        moveq   #0,d0
        move.w  (a2)+,d0                 ; get length
        beq.s   cfd_exit
cfd_loop
        move.b  (a2)+,d3                 ; get next character
        cmp.b   #'%',d3                  ; is it '%'
        bne.s   cfd_dolr                 ; ... no, check '$'

        moveq   #2,d4
        lea     cv_fditb,a0
        bra.s   cfd_group

cfd_dolr
        cmp.b   #'$',d3                  ; is it '$'
        bne.s   cfd_newl                 ; ... no, perhaps a newline

        moveq   #3,d4                    ; it is three letter
        lea     cv_fdstb,a0
cfd_group
        move.w  #$00df,d3                ; uppercase char
        and.b   (a2)+,d3
        subq.l  #1,d0
cfd_glook
        cmp.b   (a0)+,d3                 ; our character
        beq.s   cfd_gfnd                 ; found it
        tst.b   (a0)+                    ; next one
        bge.s   cfd_glook

        bra.s   cfd_char                 ; just a normal char

cfd_gfnd
        add.w   d4,d1                    ; length of line
        bra.s   cfd_next

cfd_newl
        cmp.b   #'\',d3                  ; is it newline?
        bne.s   cfd_char
        subq.l  #1,d0
        cmp.b   #'\',(a2)+               ; another \?
        beq.s   cfd_char                 ; ... yes, just put it in
        cmp.w   d1,d5                    ; newline, is this the longest?
        bge.s   cfd_rsln
        move.w  d1,d5                    ; ... yes, keep it
cfd_rsln
        moveq   #0,d1
        addq.w  #1,d2                    ; one more line
        subq.l  #1,a2                    ; back to next char
        bra.s   cfd_loop                 ; next 

cfd_char
        addq.w  #1,d1                    ; one more character
cfd_next
        assert  err.nc,-1
        subq.l  #1,d0                    ; and see if any left in format
        bgt.s   cfd_loop

        cmp.w   d5,d1                    ; longest line?
        bge.s   cfd_exit                 ; ... d1
        move.w  d5,d1                    ; ... d5

cfd_exit
        movem.l (sp)+,reg.fdsz
        rts
        end

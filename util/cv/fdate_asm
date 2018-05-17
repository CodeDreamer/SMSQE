; Format date  V2.00           1988   Tony Tebby   QJUMP

        section cv

        xdef    cv_fdate

        xref    cv_fditb
        xref    cv_fdstb

        include 'dev8_keys_err'
        include 'dev8_keys_k'
        include 'dev8_keys_qlv'
        include 'dev8_keys_dat'
        include 'dev8_mac_assert'

;+++
; Format date using format string
;
;       d1 c  p date
;       a1 c  p pointer to date buffer to put formatted string into
;       a2 c  p pointer to format string
;       status return zero or err.nc
;---
cv_fdate
reg.fdat reg    d1/d2/a0/a1/a2/a3/a6
stk_date equ    dat.buf+$0c
        movem.l reg.fdat,-(sp)
        sub.l   a6,a6                    ; utility is rel a6
        move.l  sp,a1                    ; use stack buffer from top down
        sub.w   #dat.buf,sp
        move.w  cv.ildat,a3              ; to convert date into
        jsr     (a3)
        addq.l  #2,a1                    ; ignore length
        move.w  cv.ilday,a3              ; and day into
        jsr     (a3)
        addq.l  #2,a1                    ; ignore length

        cmp.b   #'0',dat_iday(a1)        ; compress leading zeros
        bne.s   cfd_hrz
        move.b  #' ',dat_iday(a1)
cfd_hrz
        cmp.b   #'0',dat_hour(a1)
        bne.s   cfd_copy
        move.b  #' ',dat_hour(a1)

cfd_copy
        move.l  stk_date(sp),a3          ; pointer to date buffer
        addq.l  #2,a3                    ; space for length
        moveq   #0,d0
        move.w  (a2)+,d0                 ; get length
        beq.s   cfd_exit
cfd_loop
        move.b  (a2)+,d1                 ; get next character
        cmp.b   #'%',d1                  ; is it '%'
        bne.s   cfd_dolr                 ; ... no, check '$'

        moveq   #2,d2
        lea     cv_fditb,a0
        bra.s   cfd_group

cfd_dolr
        cmp.b   #'$',d1                  ; is it '$'
        bne.s   cfd_newl                 ; ... no, perhaps a newline

        moveq   #3,d2                    ; it is three letter
        lea     cv_fdstb,a0
cfd_group
        move.w  #$00df,d1                ; uppercase char
        and.b   (a2)+,d1
        subq.l  #1,d0
cfd_glook
        cmp.b   (a0)+,d1                 ; our character
        beq.s   cfd_gfnd                 ; found it
        tst.b   (a0)+                    ; next one
        bge.s   cfd_glook

        move.b  -1(a2),d1                ; restore character
        bra.s   cfd_char                 ; and send it

cfd_gfnd
        move.b  (a0)+,d1                 ; start of characters
        lea     (a1,d1.w),a0             ; in buffer

cfd_gcopy
        move.b  (a0)+,(a3)+              ; copy it
        subq.w  #1,d2                    ; count
        bgt.s   cfd_gcopy
        bra.s   cfd_next

cfd_newl
        cmp.b   #'\',d1                  ; is it newline?
        bne.s   cfd_char
        moveq   #k.nl,d1
        cmp.b   #'\',(a2)                ; another \?
        bne.s   cfd_char                 ; ... no, put newline in
        move.b  (a2)+,d1                 ; ... yes, put \ in
        subq.l  #1,d0

cfd_char
        move.b  d1,(a3)+                 ; set character
cfd_next
        assert  err.nc,-1
        subq.l  #1,d0                    ; and see if any left in format
        bgt.s   cfd_loop

cfd_exit
        move.l  stk_date(sp),a1
        sub.l   a1,a3
        subq.w  #2,a3                    ; length of string
        move.w  a3,(a1)
        add.w   #dat.buf,sp
        movem.l (sp)+,reg.fdat
        rts
        end

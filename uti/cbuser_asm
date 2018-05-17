; Call routine with own bus error handler    1992 Jochen Merz

        include dev8_keys_ev
        include dev8_keys_err
        include dev8_keys_68000

        section utility

        xdef    ut_cbuser

;+++
; Call a user-supplied routine to access hardware addresses
; and ignore internal bus error handler to find out if routine succeeds.
; This routine must be called in supervisor mode!
; The routine which is to be called must not modify d3-d4 and a3, but
; it should reset d0 on success or return any other error!
;
;               Entry                           Exit
;
;       D1      call parameter                  return parameter
;       D2      call parameter                  return parameter
;       D3+                                     preserved
;
;       A0      routine to be called            return parameter
;       A1      call parameter                  return parameter
;       A2+                                     preserved
;
;       Error returns:  ERR.NIMP if bus error occured
;                       any error returned by supplied routine
;---
cbus_reg reg d3-d4/a3-a4
ut_cbuser
        movem.l cbus_reg,-(sp)
        move.w  sr,d3                   ; keep SR
        or.w    #sr.i7,sr               ; no interrupts allowed
        move.l  sp,a3                   ; keep SSP
        move.l  ev_buse,d4              ; get standard bus error
        lea     buserr,a4
        move.l  a4,ev_buse              ; and insert new one
        moveq   #err.nimp,d0            ; assume bus error
        jsr     (a0)                    ; call routine
buserr
        move.l  a3,sp                   ; restore stack
        move.l  d4,ev_buse              ; restore bus error
        move.w  d3,sr                   ; restore SR
        movem.l (sp)+,cbus_reg
        tst.l   d0
        rts

        end

; Buffering (test byte) utility  V2.00    1989  Tony Tebby   QJUMP

        section iou

        xdef    iob_tbyt

        include 'dev8_keys_buf'
        include 'dev8_keys_err'

;+++
; This routine tests for the presence of a byte in an IO buffer.
;
; This routine can be called from an interrupt server.
;
; This is a clean routine.
;
;       d0  r   status: 0, err.nc or +1 for end of file
;       d1  r   next byte
;       a2 c  p pointer to buffer header
;       all other registers preserved
;--
iob_tbyt
ibtb.reg reg    a3
        move.l  a3,-(sp)

        move.l  buf_nxtg(a2),a3          ; next to get
        tst.b   buf_nxtb(a2)             ; is it dynamic?
        bge.s   ibtb_dyn
        cmp.l   buf_nxtp(a2),a3
        beq.s   ibtb_nc                  ; nothing to come

        move.b  (a3),d1                  ; get byte out
        bra.s   ibtb_ok

ibtb_dyn
        cmp.l   buf_nxtp(a2),a3          ; any to come?
        blt.s   ibtb_gbf                 ; ... get it
        cmp.l   buf_endb(a2),a3          ; off end?
        blt.s   ibtb_nc                  ; ... no
        move.l  buf_nxtb(a2),d0          ; ... yes, find next one
        beq.s   ibtb_nc                  ; ... none
        move.l  d0,a3
        move.l  buf_nxtg(a3),d0          ; check if anything in next buffer yet
        cmp.l   buf_nxtp(a3),d0
        beq.s   ibtb_nc                  ; ... no
        move.l  d0,a3                    ; next get pointer
ibtb_gbf
        move.b  (a3)+,d1                 ; get byte out
ibtb_ok
        moveq   #0,d0
ibtb_exit
        move.l  (sp)+,a3
        rts

ibtb_nc
        tst.b   buf_eoff(a2)             ; nothing more, is it end of file?
        bmi.s   ibtb_eof
        moveq   #err.nc,d0
        bra.s   ibtb_exit
ibtb_eof
        moveq   #1,d0
        bra.s   ibtb_exit
        end

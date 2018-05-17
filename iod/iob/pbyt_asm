; Buffering (put byte) utility  V2.00    1989  Tony Tebby   QJUMP

        section iou

        xdef    iob_pbyt

        xref    iob_abuf

        include 'dev8_keys_buf'
        include 'dev8_keys_err'
;+++
; This routine performs queued or dynamic buffering for single byte output.
; This differs from simple queue handling in that the buffer / queue
; header is $20 bytes long and the end of buffer handling is done on
; entry rather than after the last byte is put in. This makes it easier
; to handle out of memory with the dynamic buffer.
;
; If A2 is modified, the pointer to the pointer to the buffer is updated
;
; This routine cannot be called from an interrupt server if the buffer
; is dynamically allocated.
;
; This is a clean routine.
;
;       d0  r   status: 0, or err.nc
;       d1 c  p byte to send
;       a1 c  u pointer to bytes to be buffered
;       a2 c  u pointer to buffer header, updated if new buffer allocated
;       all other registers preserved
;--
iob_pbyt
ibpb.reg reg    a3
        move.l  a3,-(sp)

        move.l  buf_nxtp(a2),a3          ; next place to put
        tst.b   buf_nxtb(a2)             ; is it dynamic?
        bge.s   ibpb_dyn

        move.b  d1,(a3)+                 ; put byte in
        cmp.l   buf_endb(a2),a3          ; off end?
        blt.s   ibpb_ckf                 ; ... no
        lea     buf_strt(a2),a3          ; ... yes, start again
ibpb_ckf
        cmp.l   buf_nxtg(a2),a3
        bne.s   ibpb_ok
        bra.s   ibpb_nc

ibpb_dyn
        cmp.l   buf_endb(a2),a3          ; off end?
        blt.s   ibpb_pbf                 ; ... no
        jsr     iob_abuf                 ; ... yes, allocate a new one
        bne.s   ibpb_nc                  ; ... no memory left
ibpb_pbf
        move.b  d1,(a3)+                 ; put in
ibpb_ok
        move.l  a3,buf_nxtp(a2)          ; OK, update pointer
        moveq   #0,d0
ibpb_exit
        move.l  (sp)+,a3
        rts

ibpb_nc
        moveq   #err.nc,d0
        bra.s   ibpb_exit
        end

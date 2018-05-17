; Buffering (get byte) utility  V2.00    1989  Tony Tebby   QJUMP

        section iou

        xdef    iob_gbyt

        xref    iob_fbfl
        xref    iob_fbuf

        include 'dev8_keys_buf'
        include 'dev8_keys_err'

;+++
; This routine gets a single byte from an IO buffer. If the byte is in a
; new buffer, the old buffer is added to the linked list of throwaways.
; If it is end of file, the buffer is thrown away and A2 is returned as
; either the next list of buffers or zero.
; When A2 is modified, the corresponding current output queue pointer is
; updated.
;
; This routine can be called from an interrupt server.
;
; This is a clean routine.
;
;       d0  r   status: 0, err.nc or +1 for end of file
;       d1  r   byte fetched, not modified if err.nc, eof flag if end of file
;       a2 c  u pointer to buffer header, updated if new buffer or eof
;       a6 c  p pointer to system variables
;       all other registers preserved
;--
iob_gbyt
ibgb.reg reg    a3
        move.l  a3,-(sp)

        move.l  buf_nxtg(a2),a3          ; next get
        tst.b   buf_nxtb(a2)             ; is it dynamic?
        bge.s   ibgb_dyn
        cmp.l   buf_nxtp(a2),a3
        beq.s   ibgb_nc                  ; nothing to come

        move.b  (a3)+,d1                 ; get byte out

        cmp.l   buf_endb(a2),a3          ; off end?
        blt.s   ibgb_ok                  ; ... no
        lea     buf_strt(a2),a3          ; ... yes, start again
        bra.s   ibgb_ok

ibgb_dyn
        cmp.l   buf_nxtp(a2),a3          ; any to come?
        blt.s   ibgb_gbf                 ; get next out
        cmp.l   buf_endb(a2),a3          ; off end?
        blt.s   ibgb_nc                  ; ... no
        move.l  buf_nxtb(a2),d0          ; ... yes, find next one
        beq.s   ibgb_nc                  ; ... none
        move.l  d0,a3
        move.l  buf_nxtg(a3),d0          ; check if anything in next buffer yet
        cmp.l   buf_nxtp(a3),d0
        beq.s   ibgb_nc                  ; ... no
        move.l  buf_nxtl(a2),buf_nxtl(a3) ; transfer next list pointer
        jsr     iob_fbfl
        move.l  d0,a3                    ; next get pointer
ibgb_gbf
        move.b  (a3)+,d1                 ; get byte out
ibgb_ok
        move.l  a3,buf_nxtg(a2)          ; OK, update pointer
        moveq   #0,d0
ibgb_exit
        move.l  (sp)+,a3
        rts

ibgb_nc
        move.b  buf_eoff(a2),d0          ; nothing more, is it end of file?
        bmi.s   ibgb_eof
        moveq   #err.nc,d0
        bra.s   ibgb_exit
ibgb_eof
        move.b  d0,d1
        bclr    #7,d1                    ; set eof flag
        move.l  buf_nxtl(a2),a3          ; next buffer is in next list
        jsr     iob_fbuf
        moveq   #1,d0
        bra.s   ibgb_exit
        end

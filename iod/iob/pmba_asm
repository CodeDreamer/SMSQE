; Buffering (put multi-byte atomic) utility  V2.00    1989  Tony Tebby   QJUMP

        section iou

        xdef    iob_pmba

        xref    iob_abuf

        include 'dev8_keys_buf'
        include 'dev8_keys_err'
;+++
; This routine performs queued or dynamic buffering for byte serial output.
; This is similar to IOB_PMUL, but either all the bytes are buffered
; in one go, or none are buffered at all. In the case of a static queue,
; the number of bytes should be less than the length of the queue.
; In the case of a dynamic buffer, the number of bytes should be less than
; one segment of 512 bytes. If the length is too great, the status is returned
; positive and the non-atomic IOB_PMUL should be called instead.
;
; This routine cannot be called from an interrupt server if the buffer is
; dynamically allocated.
;
; This is a clean routine.
;
;       d0  r   status: 0, err.nc or +1 for too many bytes
;       d2 c  p (long) bytes to transfer
;       a1 c  u pointer to bytes to be buffered
;       a2 c  u pointer to buffer header (updated if new buffer allocated)
;       all other registers preserved
;--
iob_pmba
ibpa.reg reg    d2/d3/a3/a4
stk_d2   equ    $00
        movem.l ibpa.reg,-(sp)
        move.l  buf_nxtp(a2),a3          ; next put
        tst.b   buf_nxtb(a2)             ; is it dynamic?
        bge.s   ibpa_dyn

        lea     buf_strt(a2),a4
        move.l  d2,d0                    ; transfer this much
        cmp.l   buf_nxtg(a2),a3          ; fill up to get pointer or end?
        blt.s   ibpa_q2                  ; ... up to get
        move.l  buf_endb(a2),d3          ; ... up to end
        cmp.l   buf_nxtg(a2),a4          ; is next get at start (= at end)?
        beq.s   ibpa_q2m                 ; ... yes, up to get

        sub.l   a3,d3
        cmp.l   d3,d0                    ; enough room?
        ble.s   ibpa_q1a                 ; ... yes
        move.l  d3,d0
ibpa_q1a
        sub.l   d0,d2                    ; this much has gone
        bra.s   ibpa_q1e
ibpa_q1l
        move.b  (a1)+,(a3)+              ; put byte
ibpa_q1e
        subq.l  #1,d0
        bge.s   ibpa_q1l

        move.l  d2,d0                    ; all gone?
        bne.s   ibpa_q2s                 ; ... no

        cmp.l   buf_endb(a2),a3          ; off end?
        blt.s   ibpa_ok                  ; ... no
        move.l  a4,a3                    ; ... yes, start again
        bra.s   ibpa_ok

ibpa_q2s
        move.l  a4,a3                    ; start again at beginning
ibpa_q2
        move.l  buf_nxtg(a2),d3          ; up to next get
ibpa_q2m
        subq.l  #1,d3                    ; but one
        sub.l   a3,d3                    ; amount of space
        blt.s   ibpa_qnc                 ; less than none
        cmp.l   d3,d0                    ; enough room?
        bgt.s   ibpa_qnc
        bra.s   ibpa_q2e
ibpa_q2l
        move.b  (a1)+,(a3)+              ; put byte
ibpa_q2e
        subq.l  #1,d0
        bge.s   ibpa_q2l

        bra.s   ibpa_ok                  ; done

ibpa_qnc
        move.l  buf_endb(a2),a3          ; end of buffer
        sub.w   #buf_strt+1,a3           ; ... length of buffer
        cmp.l   stk_d2(sp),a3            ; enough room?
        blt.s   ibpa_bffl                ; ... no
        bra.s   ibpa_nc                  ; ... there will be sometime


ibpa_abuf
        movem.l a2/a3,-(sp)              ; save the pointers to update if ok
        jsr     iob_abuf                 ; allocate a new buffer
        bne.s   ibpa_nc8                 ; ... no memory left
        move.l  a2,d0
        move.l  (sp)+,a2
        move.l  (sp)+,buf_nxtp(a2)       ; set old buffer pointers
        move.l  d0,a2

ibpa_dyn
        cmp.l   #buf.dyna-buf_strt,d2    ; too big for us?
        bgt.s   ibpa_bffl                ; ... yes

        move.l  buf_endb(a2),d3
        sub.l   a3,d3                    ; room left
        ble.s   ibpa_abuf                ; ... none

        move.l  d2,d0
        cmp.l   d3,d0                    ; enough room?
        ble.s   ibpa_pbs
        move.l  d3,d0
ibpa_pbs
        sub.l   d0,d2                    ; this much more has gone
        bra.s   ibpa_pbe
ibpa_pbl
        move.b  (a1)+,(a3)+              ; put byte
ibpa_pbe
        subq.l  #1,d0
        bge.s   ibpa_pbl

        tst.l   d2                       ; any more?
        bgt.s   ibpa_abuf                ; ... yes

ibpa_ok
        move.l  a3,buf_nxtp(a2)          ; save pointer
        moveq   #0,d0

ibpa_exit
        movem.l  (sp)+,ibpa.reg
        rts

ibpa_nc8
        addq.l  #8,sp                    ; remove pointers from stack
ibpa_nc
        moveq   #err.nc,d0
        bra.s   ibpa_exit

ibpa_bffl
        moveq   #1,d0
        bra.s   ibpa_exit
        end

; Buffering (check room) utility  V2.00    1989  Tony Tebby   QJUMP

        section iou

        xdef    iob_room

        include 'dev8_keys_buf'

;+++
; This routine finds the room in an IO queue. This should not be called for
; a dynamic buffer.
; If end of file is set, the room is zero.
;
; This routine can be called from an interrupt server.
;
; This is a clean routine.
;
;       d0  r   long room
;       a2 c  p pointer to buffer header
;       all other registers preserved
;
;       status according to d0
;--
iob_room
        tst.b   buf_eoff(a2)             ; end of file?
        blt.s   ibrm_none
        move.l  buf_nxtg(a2),d0          ; next to get
        sub.l   buf_nxtp(a2),d0          ; total room in middle
        subq.l  #1,d0                    ; less the one spare
        bge.s   ibrm_exit                ; ... ok

        add.l   buf_endb(a2),d0          ; wrapped around, add the length
        sub.l   a2,d0
        sub.l   #buf_strt,d0
ibrm_exit
        rts
ibrm_none
        moveq   #0,d0
        rts
        end

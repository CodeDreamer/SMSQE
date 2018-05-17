; SuperBASIC File DATE extensions    V2.00    1989   Tony Tebby

        section exten

****    xdef    fupdt
        xdef    fbkdt
        xdef    fvers
        xdef    set_fupdt
        xdef    set_fbkdt
        xdef    set_fvers

        xref    ut_gtlin
        xref    ut_impc3
        xref    ut_par0
        xref    ut_trap3
        xref    ut_rtfd1
        xref    err_bp

        include 'dev8_keys_qdos_io'
        include 'dev8_keys_qdos_ioa'

;+++
; Set version number
;
;     SET_FVERS #channel|\filename, value
;---
set_fvers
        moveq   #iof.vers,d7             ; version number
        moveq   #ioa.kexc,d3
        bra.s   fd_set
;+++
; Set backup date
;
;     SET_FBKDT #channel|\filename, value
;---
set_fbkdt
        moveq   #iofd.bak,d5             ; backup date
        moveq   #ioa.kshr,d3             ; modified shared
        bra.s   fd_sdate
;+++
; Set update date
;
;     SET_FUPDT #channel|\filename, value
;---
set_fupdt
        moveq   #iofd.upd,d5             ; update date
        moveq   #ioa.kexc,d3
fd_sdate
        moveq   #iof.date,d7             ; set date
fd_set
        bsr.s   fd_impch                 ; find channel id
        bne.s   fd_rts                   ; ... oops
        jsr     ut_gtlin                 ; get long integers
        bne.s   fd_rts
        moveq   #0,d1                    ; assume set current date / version
        subq.w  #1,d3
        blt.s   fd_setd1                 ; no param
        bgt.l   err_bp                   ; ... too many params
        move.l  (a6,a1.l),d1             ; set call value
fd_setd1
        move.l  d7,d0                    ; set call params
        move.l  d5,d2
        jmp     ut_trap3

;+++
; Fetch version number
;
;     value=FVERS(#channel|\filename)
;---
fvers
        moveq   #iof.vers,d7             ; version number
        bra.s   fd_fetch
;+++
; Fetch backup date
;
;     value=FBKDT(#channel|\filename)
;---
fbkdt
        moveq   #iofd.bak,d5             ; backup date
        bra.s   fd_fdate
;+++
; Fetch update date
;
;     value=FUPDT(#channel|\filename)
;---
fupdt
        moveq   #iofd.upd,d5             ; update date
fd_fdate
        moveq   #iof.date,d7             ; fetch date
fd_fetch
        moveq   #ioa.kshr,d3             ; shared input channel
        bsr.s   fd_impch                 ; find channel id
        bne.s   fd_rts                   ; ... oops
        jsr     ut_par0                  ; any params left?
        move.l  d7,d0                    ; set call params
        moveq   #iofd.get,d1
        move.l  d5,d2
        jsr     ut_trap3
        bne.s   fd_rts
        jmp     ut_rtfd1                 ; return d1

fd_impch
        jsr     ut_impc3                 ; find channel (default #3)
fd_rts
        rts
        end

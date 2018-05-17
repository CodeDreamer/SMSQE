; QL Arithmetic Operations Test   V2.01   1990  Tony Tebby  QJUMP

        section test

        xref    qr_op
        xref    qr_dup

        xref    gu_fopen
        xref    gu_flin
        xref    gu_smul
        xref    gu_iowp
        xref    gu_clra
        xref    gu_die

        include 'dev8_keys_qlv'
        include 'dev8_keys_qdos_ioa'
        include 'dev8_keys_qdos_io'

qat_dchn equ    $00
qat_aspq equ    $04
qat_asps equ    $08
qat_buff equ    $10
qat.buff equ      $40
qat_buft equ    qat_buff+qat.buff
qat_astq equ    $100
qat_asts equ    $200

        data    $400

        bra.s   start
        dc.w    0,0,$4afb
        dc.w    7,'Test QA'

con     dc.w    16,'CON_364x202a74x5'

start
        moveq   #0,d6
        lea     (a6,a4.l),a5             ; data space
        move.l  a5,a6

        move.l  #qat_astq,qat_aspq(a5)   ; arithmetic stack pointers
        move.l  #qat_asts,qat_asps(a5)

        lea     con,a0
        moveq   #ioa.kexc,d3
        jsr     gu_fopen                 ; open console
        move.l  a0,qat_dchn(a5)          ; and our input channel as well

        moveq   #7,d1                    ; add a border
        moveq   #1,d2
        moveq   #iow.defb,d0
        jsr     gu_iowp

        jsr     gu_clra                  ; and clear all

qat_loop
        move.w  #qat.buff,d1             ; buffer length
        lea     qat_buff(a5),a1
        jsr     gu_flin                  ; fetch line
        bne.l   qat_die
        cmp.b   #'*',(a1)
        beq.l   qat_done                 ; done

        cmp.b   #'-',(a1)                ; operation?
        beq.s   qat_oper

        cmp.b   #'$',(a1)                ; HEX?
        bne.s   qat_fp
        move.w  #qat_buff+6,a0           ; buffer pointer
        move.l  qat_aspq(a5),a1
        move.w  cv.hexil,a2
        jsr     (a2)                     ; convert hex to mantissa

        move.w  #qat_buff+1,a0           ; buffer pointer
        move.w  cv.hexiw,a2
        jsr     (a2)                     ; convert hex to exponent
        bra.s   qat_sval

qat_fp
        move.w  #qat_buff,a0             ; buffer pointer
        move.l  qat_aspq(a5),a1
        move.w  cv.decfp,a2
        jsr     (a2)                     ; convert to floating point
qat_sval
        move.l  a1,qat_aspq(a5)
        move.l  qat_asps(a5),a2
        subq.l  #6,a2
        move.w  (a6,a1.l),(a6,a2.l)
        move.l  2(a6,a1.l),2(a6,a2.l)
        move.l  a2,qat_asps(a5)
        bra.s   qat_print

qat_oper
        lea     qat_buff+1,a0
        lea     qat_buft,a1
        move.w  cv.hexib,a2
        jsr     (a2)
        move.b  qat_buft-1(a6),d4
        ext.w   d4

        move.w  d4,d0
        move.w  qa.op,a2
        move.l  qat_aspq(a6),a1
        jsr     (a2)
        move.l  d0,-4(a6,a1.l)
        move.l  a1,qat_aspq(a6)

        move.w  d4,d0
        move.l  qat_asps(a6),a1
        trap    #15
        jsr     qr_op
        move.l  d0,-4(a6,a1.l)
        move.l  a1,qat_asps(a6)

qat_print
        moveq   #qat_aspq,d4
        bsr.s   qat_pline
        moveq   #qat_asps,d4
        bsr.s   qat_pline

        bra.l   qat_loop

qat_pline
        move.w  #qat_buff,a0             ; output buffer
        move.l  d4,a1
        move.w  cv.ilhex,a2
        jsr     (a2)                     ; stack pointer

        move.b  #' ',(a6,a0.l)
        addq.l  #1,a0

        move.l  (a6,d4.w),a1             ; print what is here
        subq.l  #4,a1                    ; including error code
        move.w  cv.ilhex,a2
        jsr     (a2)

        move.b  #' ',(a6,a0.l)
        addq.l  #1,a0

        move.w  cv.iwhex,a2
        jsr     (a2)

        move.b  #' ',(a6,a0.l)
        addq.l  #1,a0

        move.w  cv.ilhex,a2
        jsr     (a2)

        move.b  #' ',(a6,a0.l)
        addq.l  #1,a0

        move.l  (a6,d4.w),a1             ; print what is here
        cmp.b   #$0f,(a6,a1.l)
        bhi.s   qat_ebuf
        jsr     qr_dup
        move.w  cv.fpdec,a2
        jsr     (a2)

qat_ebuf
        move.b  #$a,(a6,a0.l)
        addq.l  #1,a0

        sub.w   #qat_buff,a0
        move.w  a0,d2
        move.w  d2,d1
        move.l  qat_dchn(a5),a0
        lea     qat_buff(a5),a1
        jmp     gu_smul

qat_done
        moveq   #0,d0                    ; done
qat_die
        jmp     gu_die

        end

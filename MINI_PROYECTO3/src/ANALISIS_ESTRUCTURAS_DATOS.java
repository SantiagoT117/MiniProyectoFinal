/**
 * ANÁLISIS DE ESTRUCTURAS DE DATOS UTILIZADAS EN EL PROYECTO
 * 
 * Este documento justifica la selección de cada estructura de datos usada
 * en las nuevas funcionalidades del proyecto Dragon Quest VIII.
 * 
 * ============================================================================
 * 1. INVENTARIO - HashMap<String, Integer>
 * ============================================================================
 * 
 * ESTRUCTURA ELEGIDA: HashMap<String, Integer>
 *   - Clave: nombre del ítem (String)
 *   - Valor: cantidad en inventario (Integer)
 * 
 * COMPLEJIDAD:
 *   - Inserción: O(1)
 *   - Búsqueda: O(1)
 *   - Eliminación: O(1)
 * 
 * JUSTIFICACIÓN:
 *   ✓ Los inventarios requieren búsquedas frecuentes por nombre de ítem
 *   ✓ Necesita permitir múltiples cantidades del mismo ítem
 *   ✓ O(1) en operaciones críticas (usar ítem, añadir ítem)
 *   ✓ No requiere ordenamiento
 *   ✓ Máximo 5 ítems diferentes por héroe (pequeño tamaño)
 * 
 * ALTERNATIVAS RECHAZADAS:
 *   ✗ ArrayList: O(n) en búsqueda, ineficiente
 *   ✗ TreeMap: O(log n), ordenamiento innecesario
 *   ✗ LinkedList: O(n) en búsqueda, optimizado para inserción en extremos
 *   ✗ HashSet: No permite duplicados, necesitamos contar cantidad
 * 
 * CASOS DE USO:
 *   - inv.agregarItem("Poción", 3) → O(1)
 *   - inv.usarItem("Poción", 1) → O(1)
 *   - inv.obtenerCantidad("Poción") → O(1)
 *   - Iteración: inv.obtenerItems() → O(n) donde n=5 máximo
 * 
 * ============================================================================
 * 2. HISTORIAL DE BATALLAS - LinkedList<RegistroBatalla>
 * ============================================================================
 * 
 * ESTRUCTURA ELEGIDA: LinkedList<RegistroBatalla>
 * 
 * COMPLEJIDAD:
 *   - Inserción al final (addLast): O(1)
 *   - Acceso aleatorio: O(n)
 *   - Obtener último: O(1)
 *   - Iteración: O(n)
 * 
 * JUSTIFICACIÓN:
 *   ✓ Los historiales requieren inserción FRECUENTE al final (O(1))
 *   ✓ Acceso secuencial eficiente (ver historial completo)
 *   ✓ Fácil acceso a la última batalla (getLast)
 *   ✓ Sin necesidad de acceso aleatorio
 *   ✓ Mejor rendimiento que ArrayList para inserción final
 * 
 * ALTERNATIVAS RECHAZADAS:
 *   ✗ ArrayList: O(n) inserción al final (crecimiento dinámico)
 *   ✗ Stack: No permite acceso secuencial al historial
 *   ✗ Queue: Pensado para consumo, no para registro
 *   ✗ TreeSet: Ordenamiento innecesario, O(log n)
 * 
 * CASOS DE USO:
 *   - historial.registrarBatalla(...) → O(1) [addLast]
 *   - historial.obtenerUltimaBatalla() → O(1) [getLast]
 *   - historial.obtenerHistorial() → O(n) iteración
 *   - estadísticas de victorias/derrotas → O(n)
 * 
 * ============================================================================
 * 3. SISTEMA DE TURNOS (GREMIO) - Queue<SolicitudAtencion>
 * ============================================================================
 * 
 * ESTRUCTURA ELEGIDA: Queue (implementado con LinkedList)
 *   - FIFO: First In, First Out
 *   - offer(): añadir al final
 *   - poll(): extraer del inicio
 * 
 * COMPLEJIDAD:
 *   - Inserción (offer): O(1)
 *   - Extracción (poll): O(1)
 *   - Observación (peek): O(1)
 * 
 * JUSTIFICACIÓN:
 *   ✓ Turnos son por naturaleza FIFO (primer llegado, primero atendido)
 *   ✓ Semántica clara: "enqueue" aventurero, "dequeue" para atender
 *   ✓ Operaciones O(1) en casos de uso más frecuentes
 *   ✓ Garantiza orden justo y transparente
 *   ✓ Imposible saltarse turnos por accidente
 * 
 * ALTERNATIVAS RECHAZADAS:
 *   ✗ Stack: LIFO, incorrecto (último sería primero)
 *   ✗ LinkedList: Menos clara semánticamente, requiere más cuidado
 *   ✗ PriorityQueue: Innecesaria, el turno es temporal, no por prioridad
 *   ✗ ArrayList: O(n) para dequeue (eliminar del inicio)
 * 
 * CASOS DE USO:
 *   - gremio.solicitarAtencion("Angelo", "Reparación") → O(1)
 *   - gremio.atenderSiguiente() → O(1)
 *   - gremio.obtenerProximo() → O(1)
 *   - gremio.obtenerNumeroEnCola() → O(1)
 * 
 * ============================================================================
 * 4. REGISTRO DE AVENTUREROS - HashSet<String>
 * ============================================================================
 * 
 * ESTRUCTURA ELEGIDA: HashSet<String>
 * 
 * COMPLEJIDAD:
 *   - Inserción (add): O(1)
 *   - Búsqueda (contains): O(1)
 *   - Eliminación (remove): O(1)
 *   - Iteración: O(n)
 * 
 * JUSTIFICACIÓN:
 *   ✓ Garantiza unicidad (no hay aventureros duplicados)
 *   ✓ Búsquedas rápidas: "¿está registrado Angelo?" → O(1)
 *   ✓ No requiere ordenamiento
 *   ✓ Perfecto para verificaciones de membresía
 *   ✓ Imposible registrar el mismo aventurero dos veces
 * 
 * ALTERNATIVAS RECHAZADAS:
 *   ✗ ArrayList: Permitiría duplicados, búsqueda O(n)
 *   ✗ TreeSet: Ordenamiento innecesario O(log n)
 *   ✗ HashMap: Overkill si solo necesitamos nombres
 *   ✗ LinkedHashSet: Mantenimiento de orden innecesario
 * 
 * CASOS DE USO:
 *   - registro.registrar("Angelo") → O(1)
 *   - registro.estaRegistrado("Jessica") → O(1)
 *   - registro.obtenerTotal() → O(1)
 *   - mostrar todos → O(n)
 * 
 * ============================================================================
 * 5. SISTEMA UNDO/REDO - Stack<Accion> (dos stacks)
 * ============================================================================
 * 
 * ESTRUCTURA ELEGIDA: Dos Stack<Accion>
 *   - pilaUndo: acciones realizadas
 *   - pilaRedo: acciones deshacidas
 * 
 * COMPLEJIDAD:
 *   - push: O(1)
 *   - pop: O(1)
 *   - peek: O(1)
 * 
 * JUSTIFICACIÓN:
 *   ✓ Stack es la estructura clásica para undo/redo
 *   ✓ LIFO es semánticamente correcto:
 *     - Deshacer la ÚLTIMA acción realizada
 *     - Rehacer la ÚLTIMA acción deshecha
 *   ✓ O(1) en todas las operaciones críticas
 *   ✓ Fácil de visualizar el comportamiento
 * 
 * ALTERNATIVAS RECHAZADAS:
 *   ✗ Queue: FIFO es incorrecto, desharíamos la primera acción
 *   ✗ LinkedList: Menos clara, requiere más cuidado
 *   ✗ ArrayList: Acceso rápido pero semántica confusa
 *   ✗ Deque: Innecesariamente flexible
 * 
 * CASOS DE USO:
 *   - Acción ataque realizada → push a pilaUndo
 *   - Usuario pulsa "Deshacer" → pop pilaUndo, push pilaRedo
 *   - Usuario pulsa "Rehacer" → pop pilaRedo, push pilaUndo
 *   - Realizar nueva acción → pop pilaUndo, limpia pilaRedo
 * 
 * ============================================================================
 * RESUMEN: Tabla comparativa de selecciones
 * ============================================================================
 * 
 * Funcionalidad          | Estructura   | Por qué
 * ─────────────────────────────────────────────────────────────────────────
 * Inventario (búsqueda)  | HashMap      | O(1) en búsquedas frecuentes
 * Historial (inserción)  | LinkedList   | O(1) en inserción al final
 * Turnos (FIFO)          | Queue        | Semántica FIFO nativa
 * Aventureros (unicidad) | HashSet      | Garantiza no-duplicados O(1)
 * Undo/Redo (LIFO)       | Stack        | Semántica LIFO nativa
 * 
 * ============================================================================
 * CONCLUSIÓN
 * ============================================================================
 * 
 * Cada estructura de datos fue seleccionada específicamente para optimizar
 * las operaciones más frecuentes en su caso de uso, garantizando tanto
 * eficiencia (O(1) en operaciones críticas) como claridad semántica del código.
 */
